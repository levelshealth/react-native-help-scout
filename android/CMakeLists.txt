cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE on)

add_compile_options(
    -fexceptions
    -frtti
    -std=c++17
    -Wall
)

# This library doesn't have custom C++ code, but we need to satisfy
# the autolinking system's expectations for TurboModules

find_package(fbjni REQUIRED CONFIG)
find_package(ReactAndroid REQUIRED CONFIG)

# Check if codegen files exist before trying to compile them
set(CODEGEN_JNI_DIR ${CMAKE_CURRENT_SOURCE_DIR}/build/generated/source/codegen/jni)

if(EXISTS "${CODEGEN_JNI_DIR}")
  file(GLOB LIBRN_CODEGEN_SRC CONFIGURE_DEPENDS "${CODEGEN_JNI_DIR}/*.cpp")
  
  if(LIBRN_CODEGEN_SRC)
    add_library(${CMAKE_PROJECT_NAME} SHARED ${LIBRN_CODEGEN_SRC})
    
    target_include_directories(${CMAKE_PROJECT_NAME}
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CODEGEN_JNI_DIR}
    )
    
    target_link_libraries(${CMAKE_PROJECT_NAME}
        ReactAndroid::jsi
        ReactAndroid::turbomodulejsijni
        ReactAndroid::react_nativemodule_core
        fbjni::fbjni
    )
  else()
    # Create a dummy target if no C++ files exist yet
    add_library(${CMAKE_PROJECT_NAME} INTERFACE)
  endif()
else()
  # Create a dummy target if codegen hasn't run yet
  add_library(${CMAKE_PROJECT_NAME} INTERFACE)
endif()
