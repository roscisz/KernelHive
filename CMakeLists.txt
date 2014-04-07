# CMake minimum version
cmake_minimum_required( VERSION 2.8 )

# Set the paths policy
cmake_policy( SET CMP0015 NEW )

# Set project name
project( KernelHive )

# Process cmake files in subdirectories
add_subdirectory ( hive-common )
add_subdirectory ( hive-worker )
add_subdirectory ( hive-unit )
