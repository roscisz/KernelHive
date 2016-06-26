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

#include <chrono>
#include <commons/KhUtils.h>
#include <commons/Logger.h>
#include <opencv2/opencv.hpp>
#include <commons/DataBufferIOHelper.h>
#include <boost/filesystem.hpp>
#include <commons/KernelHiveException.h>
#include "ImageProcessor.h"
#include "../../communication/DataUploaderGridFs.h"

namespace KernelHive {

using namespace boost::filesystem;

const char* ImageProcessor::MATRIX_BUFFER = "matrixBuffer";

ImageProcessor::ImageProcessor(char **argv): OpenCLWorker(argv) {
    previewBuffer = NULL;
    bufferHistory = 1;
    internalFcc = CV_FOURCC('x', '2', '6', '4');
    internalFps = 30;
    internalExtension = ".avi";
    internalChannels = 4;
}

ImageProcessor::~ImageProcessor() {
    cleanupResources();
}

void ImageProcessor::cleanupResources() {
    if (previewBuffer != NULL) {
        delete previewBuffer;
    }
}

void ImageProcessor::initSpecific(char *const *argv) {
    imageWidth = KhUtils::atoi(nextParam(argv));
    imageHeight = KhUtils::atoi(nextParam(argv));
}

void ImageProcessor::workSpecific() {
    std::chrono::high_resolution_clock::time_point t1 = std::chrono::high_resolution_clock::now();

    threadManager->runThread(downloaders[kernelDataId]);

    int inputBuffers = datasCount / resultsCount;
    for (int i = 0; i < std::min(bufferHistory, resultsCount); i++) {
        threadManager->runThread(downloaders[dataIds[i]]);
        if (inputBuffers == 2) {
            threadManager->runThread(downloaders[dataIds[i + resultsCount]]);
        }
    }

    cv::VideoCapture reader;
    cv::VideoCapture reader2;
    cv::VideoWriter writer;

    setPercentDone(20);

    frameSize = (size_t) (imageWidth * imageHeight * internalChannels);
    previewBuffer = new SynchronizedBuffer(sizeof(PreviewObject));

    threadManager->waitForThread(downloaders[kernelDataId]);
    prepareKernel();
    double percent = 20.0;
    double step = 60.0 / resultsCount;

    cv::Mat frame;
    cv::Mat frame4(imageHeight, imageWidth, CV_8UC4);
    cv::Mat frame2;
    cv::Mat frame42(imageHeight, imageWidth, CV_8UC4);
    for (int i = 0; i < resultsCount; ++i) {
        if (i < resultsCount - bufferHistory) {
            threadManager->runThread(downloaders[dataIds[i + bufferHistory]]);
            if (inputBuffers == 2) {
                threadManager->runThread(downloaders[dataIds[i + resultsCount + bufferHistory]]);
            }
        }
        threadManager->waitForThread(downloaders[dataIds[i]]);
        std::string filename = DataBufferIOHelper::writeDataBufferToTmpFile(dataIds[i], buffers[dataIds[i]]);
        reader.open(filename);
        frames = (size_t) reader.get(CV_CAP_PROP_FRAME_COUNT);
        path p(filename);
        p.replace_extension("") += "-p";
        p.replace_extension(internalExtension.c_str());
        writer.open(p.string(), internalFcc, internalFps, cv::Size(imageWidth, imageHeight));
        if (inputBuffers == 2) {
            threadManager->waitForThread(downloaders[dataIds[i + resultsCount]]);
            std::string filename2 = DataBufferIOHelper::writeDataBufferToTmpFile(dataIds[i + resultsCount], buffers[dataIds[i + resultsCount]]);
            reader2.open(filename2);
            size_t frames2 = (size_t) reader.get(CV_CAP_PROP_FRAME_COUNT);
            if (frames != frames2) {
                throw new KernelHiveException("Different number of frames in files");
            }
            buffers[dataIds[i + resultsCount]]->allocate(frameSize);
        }

        buffers[dataIds[i]]->allocate(frameSize);
        resultBuffers[i]->allocate(frameSize);

        for (int j = 0; j < frames; j++) {
            reader >> frame;
            cv::cvtColor(frame, frame4, CV_BGR2BGRA, internalChannels);
            buffers[dataIds[i]]->seek(0);
            buffers[dataIds[i]]->append(frame4.data, frameSize);
            if (inputBuffers == 2) {
                reader2 >> frame2;
                cv::cvtColor(frame2, frame42, CV_BGR2BGRA, internalChannels);
                buffers[dataIds[i + resultsCount]]->seek(0);
                buffers[dataIds[i + resultsCount]]->append(frame42.data, frameSize);
            }
            workOnImage(i);
            resultBuffers[i]->seek(0);
            resultBuffers[i]->read(frame4.data, frameSize);
            cv::cvtColor(frame4, frame, CV_BGRA2BGR, 3);
            writer << frame;
        }
        writer.release();
        DataBufferIOHelper::readDataBufferFromTmpFile(p.string(), resultBuffers[i]);
        uploaders[i]->setSuffix(internalExtension);
        threadManager->runThread(uploaders[i]);
        buffers[dataIds[i]]->deallocate();
        if (inputBuffers == 2) {
            buffers[dataIds[i + resultsCount]]->deallocate();
        }
        if (i >= bufferHistory) {
            threadManager->waitForThread(uploaders[i - bufferHistory]);
            resultBuffers[i - bufferHistory]->deallocate();
        }
        percent += step;
        setPercentDone((int) percent);
    }

    setPercentDone(80);

    reportPreview(previewBuffer);

    waitForAllUploads();
    std::chrono::high_resolution_clock::time_point t2 = std::chrono::high_resolution_clock::now();

    setPercentDone(100);

    double duration = std::chrono::duration_cast<std::chrono::milliseconds>(t2 - t1).count() / 1000.0;
    std::stringstream ss;
    ss << "Processing took " << duration << "s" << std::endl;
    Logger::log(INFO, ss.str().c_str());
}

}
