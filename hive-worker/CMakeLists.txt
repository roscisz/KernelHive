file( GLOB sources "src/workers/*" )

# Add include files directories
include_directories( "${PROJECT_SOURCE_DIR}/hive-common/src" )

# Build the hive worker library
add_library( hiveworker SHARED ${sources} )
target_link_libraries( hiveworker hivecommons )

# Build the executables, link the built commons library to it
add_executable( SampleWorker "src/hive-worker.cpp" )
target_link_libraries( SampleWorker hiveworker )
target_link_libraries( SampleWorker hivecommons )

add_executable( DataProcessor "src/dataprocessor.cpp" )
target_link_libraries( DataProcessor hiveworker )
target_link_libraries( DataProcessor hivecommons )
target_link_libraries( DataProcessor pthread )

