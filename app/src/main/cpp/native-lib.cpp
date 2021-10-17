#include "jni.h"
#include "mapbox/polylabel.hpp"

extern "C" JNIEXPORT jdoubleArray JNICALL
Java_com_sk1982_purduemap_utils_GeometryUtils_polylabel(JNIEnv *env, jobject thiz, jobjectArray vertices) {
    jdoubleArray result = env->NewDoubleArray(2);

    mapbox::geometry::polygon<double> polygon;
    mapbox::geometry::linear_ring<double> ring;

    auto length = env->GetArrayLength(vertices);
    for (int i = 0; i < length; ++i) {
        auto vertex = reinterpret_cast<jdoubleArray>(env->GetObjectArrayElement(vertices, i));

        jdouble* body = env->GetDoubleArrayElements(vertex, nullptr);

        mapbox::geometry::point<double> point(body[0], body[1]);

        ring.push_back(point);
    }

    polygon.push_back(ring);

    mapbox::geometry::point<double> polylabel = mapbox::polylabel(polygon, 1.0);

    env->SetDoubleArrayRegion(result, 0, 1, &polylabel.x);
    env->SetDoubleArrayRegion(result, 1, 1, &polylabel.y);

    return result;
}