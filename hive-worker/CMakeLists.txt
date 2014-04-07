cmake_minimum_required(VERSION 2.8)

file( GLOB sources "src/workers/*" "src/communication/*" )


SET(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/CMakeModules/")
find_package(Boost 1.53 REQUIRED COMPONENTS system filesystem thread-mt)
find_package(MongoDB REQUIRED)

# Add include files directories
include_directories( "${PROJECT_SOURCE_DIR}/hive-common/src" )
include_directories(${MongoDB_INCLUDE_DIR})

# Build the hive worker library
add_library( hiveworker SHARED ${sources} )
target_link_libraries( hiveworker ${Boost_LIBRARIES} ${MongoDB_LIBRARIES})

# Build the executables, link the built commons library to them
add_executable( SampleWorker "src/hive-worker.cpp" )
target_link_libraries( SampleWorker hiveworker )
target_link_libraries( SampleWorker hivecommons )

add_executable( DataProcessor "src/dataprocessor.cpp" )
target_link_libraries( DataProcessor hiveworker )
target_link_libraries( DataProcessor hivecommons )
target_link_libraries( DataProcessor pthread )

add_executable( DataMerger "src/datamerger.cpp" )
target_link_libraries( DataMerger hiveworker )
target_link_libraries( DataMerger hivecommons )
target_link_libraries( DataMerger pthread )

add_executable( DataPartitioner "src/datapartitioner.cpp" )
target_link_libraries( DataPartitioner hiveworker )
target_link_libraries( DataPartitioner hivecommons )
target_link_libraries( DataPartitioner pthread )

add_executable( DataServerTester "src/dataservertester.cpp" )
target_link_libraries( DataServerTester hiveworker )
target_link_libraries( DataServerTester hivecommons )
target_link_libraries( DataServerTester pthread )

