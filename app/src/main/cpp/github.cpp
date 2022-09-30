#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_saeware_github_config_Config_apiKey(JNIEnv *env, jobject object) {
   std::string api_key = "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";

   return env->NewStringUTF(api_key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_saeware_github_config_Config_baseUrl(JNIEnv *env, jobject object) {
   std::string base_url = "https://api.github.com/";

   return env->NewStringUTF(base_url.c_str());
}