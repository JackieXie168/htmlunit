/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.general.huge;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.google.common.base.Predicate;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'W' to 'Z'.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfWTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(new Predicate<String>() {

            @Override
            public boolean apply(final String input) {
                final char ch = Character.toUpperCase(input.charAt(0));
                return ch >= 'W' && ch <= 'Z';
            }
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebSocket_WebSocket() throws Exception {
        test("WebSocket", "WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Window_Window() throws Exception {
        test("Window", "Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLDocument_XMLDocument() throws Exception {
        test("XMLDocument", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLHttpRequest_XMLHttpRequest() throws Exception {
        test("XMLHttpRequest", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLSerializer_XMLSerializer() throws Exception {
        test("XMLSerializer", "XMLSerializer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathEvaluator_XPathEvaluator() throws Exception {
        test("XPathEvaluator", "XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathResult_XPathResult() throws Exception {
        test("XPathResult", "XPathResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XSLTProcessor_XSLTProcessor() throws Exception {
        test("XSLTProcessor", "XSLTProcessor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Worker_Worker() throws Exception {
        test("Worker", "Worker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLRenderingContext_WebGLRenderingContext() throws Exception {
        test("WebGLRenderingContext", "WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _WaveShaperNode_WaveShaperNode() throws Exception {
        test("WaveShaperNode", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WheelEvent_WheelEvent() throws Exception {
        test("WheelEvent", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WeakMap_WeakMap() throws Exception {
        test("WeakMap", "WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _WeakSet_WeakSet() throws Exception {
        test("WeakSet", "WeakSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitRTCPeerConnection_webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection", "webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    @NotYetImplemented(IE)
    public void _XMLHttpRequestEventTarget_XMLHttpRequest() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLHttpRequestEventTarget_XMLHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechRecognition_webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition", "webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLActiveInfo_WebGLActiveInfo() throws Exception {
        test("WebGLActiveInfo", "WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLBuffer_WebGLBuffer() throws Exception {
        test("WebGLBuffer", "WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
    public void _WebGLContextEvent_WebGLContextEvent() throws Exception {
        test("WebGLContextEvent", "WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLFramebuffer_WebGLFramebuffer() throws Exception {
        test("WebGLFramebuffer", "WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLProgram_WebGLProgram() throws Exception {
        test("WebGLProgram", "WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLRenderbuffer_WebGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer", "WebGLRenderbuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLShaderPrecisionFormat_WebGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat", "WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLShader_WebGLShader() throws Exception {
        test("WebGLShader", "WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLTexture_WebGLTexture() throws Exception {
        test("WebGLTexture", "WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _WebGLUniformLocation_WebGLUniformLocation() throws Exception {
        test("WebGLUniformLocation", "WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitCSSMatrix_WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix", "WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XMLHttpRequestEventTarget_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XMLHttpRequestUpload_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload", "XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathExpression_XPathExpression() throws Exception {
        test("XPathExpression", "XPathExpression");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitAnimationEvent_AnimationEvent() throws Exception {
        test("WebKitAnimationEvent", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitAnimationEvent_WebKitAnimationEvent() throws Exception {
        test("WebKitAnimationEvent", "WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitMutationObserver_MutationObserver() throws Exception {
        test("WebKitMutationObserver", "MutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitMutationObserver_WebKitMutationObserver() throws Exception {
        test("WebKitMutationObserver", "WebKitMutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitTransitionEvent_TransitionEvent() throws Exception {
        test("WebKitTransitionEvent", "TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitTransitionEvent_WebKitTransitionEvent() throws Exception {
        test("WebKitTransitionEvent", "WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitAudioContext_AudioContext() throws Exception {
        test("webkitAudioContext", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _webkitAudioContext_OfflineAudioContext() throws Exception {
        test("webkitAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitAudioContext_webkitAudioContext() throws Exception {
        test("webkitAudioContext", "webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _webkitAudioContext_webkitOfflineAudioContext() throws Exception {
        test("webkitAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBCursor_IDBCursor() throws Exception {
        test("webkitIDBCursor", "IDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBCursor_IDBCursorWithValue() throws Exception {
        test("webkitIDBCursor", "IDBCursorWithValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBCursor_webkitIDBCursor() throws Exception {
        test("webkitIDBCursor", "webkitIDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBDatabase_IDBDatabase() throws Exception {
        test("webkitIDBDatabase", "IDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBDatabase_webkitIDBDatabase() throws Exception {
        test("webkitIDBDatabase", "webkitIDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBFactory_IDBFactory() throws Exception {
        test("webkitIDBFactory", "IDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBFactory_webkitIDBFactory() throws Exception {
        test("webkitIDBFactory", "webkitIDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBIndex_IDBIndex() throws Exception {
        test("webkitIDBIndex", "IDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBIndex_webkitIDBIndex() throws Exception {
        test("webkitIDBIndex", "webkitIDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBKeyRange_IDBKeyRange() throws Exception {
        test("webkitIDBKeyRange", "IDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBKeyRange_webkitIDBKeyRange() throws Exception {
        test("webkitIDBKeyRange", "webkitIDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBObjectStore_IDBObjectStore() throws Exception {
        test("webkitIDBObjectStore", "IDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBObjectStore_webkitIDBObjectStore() throws Exception {
        test("webkitIDBObjectStore", "webkitIDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBRequest_IDBOpenDBRequest() throws Exception {
        test("webkitIDBRequest", "IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBRequest_IDBRequest() throws Exception {
        test("webkitIDBRequest", "IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBRequest_webkitIDBRequest() throws Exception {
        test("webkitIDBRequest", "webkitIDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBTransaction_IDBTransaction() throws Exception {
        test("webkitIDBTransaction", "IDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitIDBTransaction_webkitIDBTransaction() throws Exception {
        test("webkitIDBTransaction", "webkitIDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _webkitMediaStream_MediaStream() throws Exception {
        test("webkitMediaStream", "MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitMediaStream_webkitMediaStream() throws Exception {
        test("webkitMediaStream", "webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitOfflineAudioContext_OfflineAudioContext() throws Exception {
        test("webkitOfflineAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitOfflineAudioContext_webkitOfflineAudioContext() throws Exception {
        test("webkitOfflineAudioContext", "webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechGrammarList_webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList", "webkitSpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechGrammar_webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar", "webkitSpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechRecognitionError_webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError", "webkitSpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechRecognitionEvent_webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent", "webkitSpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitURL_URL() throws Exception {
        test("webkitURL", "URL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitURL_webkitURL() throws Exception {
        test("webkitURL", "webkitURL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _WEBGL_compressed_texture_s3tc_WEBGL_compressed_texture_s3tc() throws Exception {
        test("WEBGL_compressed_texture_s3tc", "WEBGL_compressed_texture_s3tc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _WEBGL_debug_renderer_info_WEBGL_debug_renderer_info() throws Exception {
        test("WEBGL_debug_renderer_info", "WEBGL_debug_renderer_info");
    }
}
