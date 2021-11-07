package com.github.sebyplays.jwebserver.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentType {

    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_X_JAVASCRIPT("application/x-javascript"),
    APPLICATION_X_LATEX("application/x-latex"),
    APPLICATION_X_SHOCKWAVE_FLASH("application/x-shockwave-flash"),
    APPLICATION_X_TEX("application/x-tex"),
    APPLICATION_X_TEXINFO("application/x-texinfo"),
    APPLICATION_X_TEX_PREAMBLE("application/x-tex-preamble"),
    APPLICATION_X_TEX_TANGLE("application/x-tex-tangle"),
    APPLICATION_X_TROFF("application/x-troff"),

    //list of audio content types
    AUDIO_MPEG("audio/mpeg"),
    AUDIO_X_MS_WMA("audio/x-ms-wma"),
    AUDIO_VND_RN_REALAUDIO("audio/vnd.rn-realaudio"),
    AUDIO_X_WAV("audio/x-wav"),

    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_TIFF("image/tiff"),
    IMAGE_VND_MICROSOFT_ICON("image/vnd.microsoft.icon"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_VND_DJVU("image/vnd.djvu"),
    IMAGE_SVG_XML("image/svg+xml"),

    //multipart http content-types
    MULTIPART_FORM_DATA("multipart/form-data"),
    MULTIPART_MIXED("multipart/mixed"),
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    MULTIPART_RELATED("multipart/related"),

    //list of text content types
    TEXT_CSS("text/css"),
    TEXT_CSV("text/csv"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    TEXT_X_JAVASCRIPT("text/x-javascript"),
    TEXT_X_JAVASCRIPT_SOURCE("text/x-javascript-source"),
    TEXT_X_LATEX("text/x-latex"),
    TEXT_X_SCRIPT_SH("text/x-script.sh"),
    TEXT_X_SCRIPT_ZSH("text/x-script.zsh"),
    TEXT_X_SERVICE_WORKER_JS("text/x-service-worker-js"),
    TEXT_X_TEX("text/x-tex"),
    TEXT_X_TEXINFO("text/x-texinfo"),
    TEXT_X_TROFF("text/x-troff"),

    //list of video content types
    VIDEO_MPEG("video/mpeg"),
    VIDEO_MP4("video/mp4"),
    VIDEO_QUICKTIME("video/quicktime"),
    VIDEO_X_MSVIDEO("video/x-msvideo"),
    VIDEO_X_MS_WMV("video/x-ms-wmv"),
    VIDEO_X_FLV("video/x-flv"),
    VIDEO_X_MATROSKA("video/x-matroska"),
    VIDEO_X_MS_ASF("video/x-ms-asf"),
    VIDEO_X_MS_WMV_3D("video/x-ms-wmv-3d"),
    VIDEO_X_MS_WMV_3D_V2("video/x-ms-wmv-3d-v2"),
    VIDEO_X_MS_WMV_V2("video/x-ms-wmv-v2"),
    VIDEO_X_MS_WMV_V3("video/x-ms-wmv-v3"),
    VIDEO_X_MS_WMV_V4("video/x-ms-wmv-v4"),
    VIDEO_X_MS_WMV_V5("video/x-ms-wmv-v5"),
    VIDEO_X_MS_WMV_V6("video/x-ms-wmv-v6"),
    VIDEO_X_MS_WMV_V7("video/x-ms-wmv-v7"),
    VIDEO_X_MS_WMV_V8("video/x-ms-wmv-v8"),
    VIDEO_X_MS_WMV_V9("video/x-ms-wmv-v9"),
    VIDEO_X_MS_WMV_V10("video/x-ms-wmv-v10"),
    VIDEO_X_MS_WMV_V11("video/x-ms-wmv-v11"),
    VIDEO_X_MS_WMV_V12("video/x-ms-wmv-v12"),
    VIDEO_X_MS_WMV_V13("video/x-ms-wmv-v13"),
    VIDEO_X_MS_WMV_V14("video/x-ms-wmv-v14"),
    VIDEO_X_MS_WMV_V15("video/x-ms-wmv-v15");



    @Getter @NonNull public String contentType;

}


