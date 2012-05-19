# Add include files dorectory
include_directories( "${PROJECT_SOURCE_DIR}/hive-common/src" )

# Build the tester executable, link the built library to it
add_executable( SimpleWorker "src/hive-worker.cpp" )
target_link_libraries( SimpleWorker hivecommons )

