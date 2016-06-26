cmake_minimum_required(VERSION 2.8)

file( GLOB sources "src/workers/*" "src/communication/*" "src/workers/image/*" )

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/CMakeModules/")
find_package(Boost 1.53 REQUIRED COMPONENTS system filesystem thread)
find_package(OpenCV REQUIRED)
find_package(MongoDB REQUIRED)

# Add include files directories
include_directories( "${PROJECT_SOURCE_DIR}/hive-common/src" )
include_directories(${MongoDB_INCLUDE_DIR})

# Build the hive worker library
add_library( hiveworker SHARED ${sources} )
target_link_libraries( hiveworker ${Boost_LIBRARIES} ${MongoDB_LIBRARIES} ${OpenCV_LIBS} )

# Build the executables, link the built commons library to them
add_executable( SampleWorker "src/hive-worker.cpp" )
target_link_libraries( SampleWorker hiveworker )
target_link_libraries( SampleWorker hivecommons )
target_link_libraries( SampleWorker ssl )

add_executable( DataProcessor "src/dataprocessor.cpp" )
target_link_libraries( DataProcessor hiveworker )
target_link_libraries( DataProcessor hivecommons )
target_link_libraries( DataProcessor pthread )
target_link_libraries( DataProcessor ssl )

add_executable( DataMerger "src/datamerger.cpp" )
target_link_libraries( DataMerger hiveworker )
target_link_libraries( DataMerger hivecommons )
target_link_libraries( DataMerger pthread )
target_link_libraries( DataMerger ssl )

add_executable( DataPartitioner "src/datapartitioner.cpp" )
target_link_libraries( DataPartitioner hiveworker )
target_link_libraries( DataPartitioner hivecommons )
target_link_libraries( DataPartitioner pthread )
target_link_libraries( DataPartitioner ssl )

add_executable( DataServerTester "src/dataservertester.cpp" )
target_link_libraries( DataServerTester hiveworker )
target_link_libraries( DataServerTester hivecommons )
target_link_libraries( DataServerTester pthread )
target_link_libraries( DataServerTester ssl )

add_executable( ImagePartitioner "src/imageparitioner.cpp")
target_link_libraries( ImagePartitioner hiveworker )
target_link_libraries( ImagePartitioner hivecommons )
target_link_libraries( ImagePartitioner pthread )
target_link_libraries( ImagePartitioner ssl )

add_executable( ImageMerger "src/imagemerger.cpp")
target_link_libraries( ImageMerger hiveworker )
target_link_libraries( ImageMerger hivecommons )
target_link_libraries( ImageMerger pthread )
target_link_libraries( ImageMerger ssl )

add_executable( ConverterImageProcessor "src/converterimageprocessor.cpp")
target_link_libraries( ConverterImageProcessor hiveworker )
target_link_libraries( ConverterImageProcessor hivecommons )
target_link_libraries( ConverterImageProcessor pthread )
target_link_libraries( ConverterImageProcessor ssl )

add_executable( ConvolutionImageProcessor "src/convolutionimageprocessor.cpp")
target_link_libraries( ConvolutionImageProcessor hiveworker )
target_link_libraries( ConvolutionImageProcessor hivecommons )
target_link_libraries( ConvolutionImageProcessor pthread )
target_link_libraries( ConvolutionImageProcessor ssl )

add_executable( SumImageProcessor "src/sumimageprocessor.cpp")
target_link_libraries( SumImageProcessor hiveworker )
target_link_libraries( SumImageProcessor hivecommons )
target_link_libraries( SumImageProcessor pthread )
target_link_libraries( SumImageProcessor ssl )

add_executable( SobelImageProcessor "src/sobelimageprocessor.cpp")
target_link_libraries( SobelImageProcessor hiveworker )
target_link_libraries( SobelImageProcessor hivecommons )
target_link_libraries( SobelImageProcessor pthread )
target_link_libraries( SobelImageProcessor ssl )