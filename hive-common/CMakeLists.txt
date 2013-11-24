# Set all source files to a variable
file( GLOB sources "src/commons/*" "src/network/*" "src/threading/*" )

# Build the library, link OpenCL to it
add_library( hivecommons SHARED ${sources} )
target_link_libraries( hivecommons OpenCL )
target_link_libraries( hivecommons pthread )
SET(GCC_OPENCL_DEPRECATED_COMPILE_FLAGS "-DCL_USE_DEPRECATED_OPENCL_1_1_APIS")
SET( CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} ${GCC_OPENCL_DEPRECATED_COMPILE_FLAGS}" )

# Build the tester executable, link the built library to it
add_executable( DevicesInfo "src/Main.cpp" )
add_executable( KernelCompiler "src/Compiler.cpp" )
target_link_libraries( DevicesInfo hivecommons )
target_link_libraries( KernelCompiler hivecommons )
