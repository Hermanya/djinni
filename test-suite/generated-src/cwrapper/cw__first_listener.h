// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from listeners.djinni

#pragma once // python_cdef_ignore
#include <stdbool.h>  // python_cdef_ignore

struct DjinniWrapperFirstListener;
void first_listener___delete(struct DjinniObjectHandle * djinni_this);
void first_listener___wrapper_dec_ref(struct DjinniWrapperFirstListener * dh);
void first_listener___wrapper_add_ref(struct DjinniWrapperFirstListener * dh);

struct DjinniWrapperFirstListener *  make_proxy_object_from_handle_cw__first_listener(struct DjinniObjectHandle * c_ptr);
struct DjinniObjectHandle * get_handle_from_proxy_object_cw__first_listener(struct DjinniWrapperFirstListener * dw);
bool equal_handles_cw__first_listener(struct DjinniWrapperFirstListener * dw1, struct DjinniWrapperFirstListener * dw2);

void first_listener_add_callback_first(void(* ptr)(struct DjinniObjectHandle * ));

void first_listener_add_callback___delete(void(* ptr)(struct DjinniObjectHandle * ));
