# Set all source files to a variable
file( GLOB sources "src/commons/*" "src/network/*" "src/threading/*" )

# Build the library, link OpenCL to it
add_library( hivecommons SHARED ${sources} )
target_link_libraries( hivecommons OpenCL )
target_link_libraries( hivecommons pthread )

# Build the tester executable, link the built library to it
add_executable( DevicesInfo "src/Main.cpp" )
target_link_libraries( DevicesInfo hivecommons )

