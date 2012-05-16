# Set all source files to a variable
file( GLOB sources "src/commons/*" "src/network/*" "src/threading/*" )

# Build the library, link OpenCL to it
add_library( Commons STATIC ${sources} )
target_link_libraries( Commons OpenCL )

# Build the tester executable, link the built library to it
add_executable( Tester "src/Main.cpp" )
target_link_libraries( Tester Commons )

