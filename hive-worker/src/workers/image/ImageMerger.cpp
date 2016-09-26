/**
* Copyright (c) 2016 Gdansk University of Technology
* Copyright (c) 2016 Adrian Boguszewski
*
* This file is part of KernelHive.
* KernelHive is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 3 of the License, or
* (at your option) any later version.
*
* KernelHive is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
*/


#include <opencv2/opencv.hpp>
#include <boost/filesystem.hpp>
#include <commons/KernelHiveException.h>
#include <fstream>
#include <commons/KhUtils.h>
#include <chrono>
#include <commons/Logger.h>
#include <commons/DataBufferIOHelper.h>
#include "ImageMerger.h"
#include "../../communication/DataUploaderGridFs.h"

namespace KernelHive {

using namespace boost::filesystem;

ImageMerger::ImageMerger(char **argv): BasicWorker(argv) {
    bufferHistory = 2;
    internalExtension = ".avi";
}

ImageMerger::~ImageMerger() {

}

void ImageMerger::work(char *const *argv) {
    init(argv);

    if (resultsCount != 1) {
        throw new KernelHiveException("VideoEncoder supports only one output file!");
    }

    std::chrono::high_resolution_clock::time_point t1 = std::chrono::high_resolution_clock::now();
    // start downloading data
    for (int i = 0; i < std::min(bufferHistory, datasCount); i++) {
        threadManager->runThread(downloaders[dataIds[i]]);
    }
    setPercentDone(20);
    // need to create tmp file, because opencv cannot decode video from memory buffer...
    std::string filepath = merge();
    DataBufferIOHelper::readDataBufferFromTmpFile(filepath, resultBuffers[0]);
    setPercentDone(90);

    // upload the result
    uploaders[0]->setSuffix(internalExtension);
    runAllUploadsSync();
    setPercentDone(100);
    std::chrono::high_resolution_clock::time_point t2 = std::chrono::high_resolution_clock::now();

    double duration = std::chrono::duration_cast<std::chrono::milliseconds>(t2 - t1).count() / 1000.0;
    std::stringstream ss;
    ss << "Processing took " << duration << "s" << std::endl;
    Logger::log(INFO, ss.str().c_str());

    std::string tmp;
    uploaders[0]->getDataURL(&tmp);
    reportOver(tmp.c_str());
}

std::string ImageMerger::merge() {
    // path to /tmp/filename-w.extension
    path p = temp_directory_path();
    p /= dataIds[0];
    p.replace_extension();
    p += "-w";
    p.replace_extension(path(fileExtension));

    cv::VideoWriter writer;
    cv::VideoCapture reader;

    int fcc = CV_FOURCC(fourcc.at(0), fourcc.at(1), fourcc.at(2), fourcc.at(3));

    writer.open(p.string(), fcc, fps, cv::Size(frameWidth, frameHeight));
    if (!writer.isOpened()) {
        throw new KernelHiveException("Cannot open video file to encode");
    }

    double percent = 20.0;
    double step = 60.0 / datasCount;
    cv::Mat frame(frameHeight, frameWidth, CV_8UC3);
    for (int i = 0; i < datasCount; i++) {
        if (i < datasCount - bufferHistory) {
            threadManager->runThread(downloaders[dataIds[i + bufferHistory]]);
        }
        threadManager->waitForThread(downloaders[dataIds[i]]);

        // write this chunk to file
        std::string filename = DataBufferIOHelper::writeDataBufferToTmpFile(dataIds[i], buffers[dataIds[i]]);
        buffers[dataIds[i]]->deallocate();

        reader.open(filename);
        while(true) {
            reader >> frame;
            if (frame.empty()) {
                break;
            }
            writer << frame;
        }
        percent += step;
        setPercentDone((int) percent);
    }

    return p.string();
}

void ImageMerger::init(char *const *argv) {
    BasicWorker::init(argv);
    frameWidth = KhUtils::atoi(nextParam(argv));
    frameHeight = KhUtils::atoi(nextParam(argv));
    fps = KhUtils::atoi(nextParam(argv));
    fourcc = nextParam(argv);
    fileExtension = nextParam(argv);
}

}
