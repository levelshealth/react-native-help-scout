require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = 'ReactNativeHelpScout'
  s.version      = package['version']
  s.summary      = package['description']

  s.authors      = { 'Dimitar Nestorov': 'opensource@dimitarnestorov.com' }
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, '11.0'

  s.module_name  = 'ReactNativeHelpScout'

  s.source       = { :git => 'https://github.com/codemotionapps/react-native-help-scout.git', :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,m,mm}"
  
  # Install dependencies for new architecture
  install_modules_dependencies(s)
  
  # New architecture configuration  
  s.compiler_flags = folly_flags() + " -DRCT_NEW_ARCH_ENABLED=1 -fmodules -fcxx-modules"
  s.pod_target_xcconfig = {
    "HEADER_SEARCH_PATHS" => "$(inherited) \"$(PODS_ROOT)/RCT-Folly\" \"$(PODS_ROOT)/boost\" \"$(PODS_ROOT)/DoubleConversion\"",
    "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
    "OTHER_CPLUSPLUSFLAGS" => "$(inherited) -DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1"
  }
  
  s.libraries = 'c++'
  s.dependency 'Beacon', '~> 3.0.1'
  s.frameworks = 'UIKit'
  s.static_framework = true
end
