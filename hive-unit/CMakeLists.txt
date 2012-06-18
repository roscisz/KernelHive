file( GLOB sources "src/*" )

# Add include files directories
include_directories( "${PROJECT_SOURCE_DIR}/hive-common/src" )

# Build the hive unit library
add_library( hiveunit SHARED ${sources} )
target_link_libraries( hiveunit hivecommons )

# Build the executables, link the built commons library to it
add_executable( HiveUnit "src/hive-unit.cpp" )
target_link_libraries( HiveUnit hiveunit )

