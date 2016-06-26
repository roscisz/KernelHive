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

#include "ImagePartitioner.h"
#include <fstream>
#include <sstream>
#include <commons/KernelHiveException.h>
#include <chrono>
#include <boost/filesystem.hpp>
#include <commons/Logger.h>
#include <commons/DataBufferIOHelper.h>
#include <commons/KhUtils.h>
#include "opencv2/opencv.hpp"
#include "../../communication/DataUploaderGridFs.h"

namespace KernelHive {

using namespace boost::filesystem;

ImagePartitioner::ImagePartitioner(char **argv): BasicWorker(argv) {
    bufferHistory = 2;
    internalFcc = CV_FOURCC('x', '2', '6', '4');
    internalExtension = ".avi";
}

ImagePartitioner::~ImagePartitioner() {

}

void ImagePartitioner::work(char *const *argv) {
    init(argv);

    if (datasCount != 1) {
        throw new KernelHiveException("VideoDecoder supports only one input file!");
    }

    std::chrono::high_resolution_clock::time_point t1 = std::chrono::high_resolution_clock::now();
    // download the data
    runAllDownloads();
    waitForAllDownloads();
    setPercentDone(10);

    // need to create tmp file, because opencv cannot decode video from memory buffer...
    std::string filepath = DataBufferIOHelper::writeDataBufferToTmpFile(dataIds[0], buffers[dataIds[0]]);
    setPercentDone(20);
    partition(filepath);

    // upload the result
    waitForAllUploads();
    setPercentDone(100);
    std::chrono::high_resolution_clock::time_point t2 = std::chrono::high_resolution_clock::now();

    double duration = std::chrono::duration_cast<std::chrono::milliseconds>(t2 - t1).count() / 1000.0;
    std::stringstream ss;
    ss << "Processing took " << duration << "s" << std::endl;
    Logger::log(INFO, ss.str().c_str());

    std::string tmp;
    getAllUploadIDStrings(&tmp);
    reportOver(tmp.c_str());
}

void ImagePartitioner::partition(const std::string &filename) {
    cv::VideoCapture reader;
    cv::VideoWriter writer;

    // path to /tmp/filename-?
    path p = temp_directory_path();
    p /= dataIds[0];
    p.replace_extension();
    p += "-";

    reader.open(filename);
    if (!reader.isOpened()) {
        throw new KernelHiveException("Cannot open reader file to decode");
    }

    // read some properties
    int frameWidth = (int) reader.get(CV_CAP_PROP_FRAME_WIDTH);
    int frameHeight = (int) reader.get(CV_CAP_PROP_FRAME_HEIGHT);
    int frameNumber = (int) reader.get(CV_CAP_PROP_FRAME_COUNT);
    int fps = (int) reader.get(CV_CAP_PROP_FPS);
    int fourcc = (int) reader.get(CV_CAP_PROP_FOURCC);

    // debug: print video parameters
    // std::cout << frameWidth << " " << frameHeight << " " << mandatoryNumberOfChannels << " " << fps << " " << fourcc << std::endl;
    int remainingFrames = frameNumber;

    double percent = 20.0;
    double step = 60.0 / resultsCount;
    cv::Mat frame;
    for (int i = 0; i < resultsCount; i++) {
        int framesInThisBuffer = remainingFrames / (resultsCount - i);

        // tmp file for this chunk
        path tmpPath = p;
        tmpPath += KhUtils::itoa(i);
        tmpPath += internalExtension;
        writer.open(tmpPath.string(), internalFcc, fps, cv::Size(frameWidth, frameHeight));

        for (int j = 0; j < framesInThisBuffer; j++) {
            reader >> frame;
            if (frame.empty()) {
                break;
            }
            writer << frame;
        }
        // flush data to file
        writer.release();
        // load data to memory buffer
        DataBufferIOHelper::readDataBufferFromTmpFile(tmpPath.string(), resultBuffers[i]);
        // upload the package
        uploaders[i]->setSuffix(internalExtension);
        threadManager->runThread(uploaders[i]);
        // wait for previous uploader and deallocate memory
        if (i >= bufferHistory) {
            threadManager->waitForThread(uploaders[i - bufferHistory]);
            resultBuffers[i - bufferHistory]->deallocate();
        }
        remainingFrames -= framesInThisBuffer;
        percent += step;
        setPercentDone((int) percent);
    }
}

}
