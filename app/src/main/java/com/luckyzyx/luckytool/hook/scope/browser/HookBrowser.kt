package com.luckyzyx.luckytool.hook.scope.browser

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object HookBrowser : YukiBaseHooker() {
    override fun onHook() {
        //Source WrappedMCWebViewClient
        findClass("com.heytap.browser.webview.WrappedMCWebViewClient").hook {
            injectMember {
                method { name { it.startsWith("onPage") } }
                afterHook {
                    val currentWebView = args().first().any()?.current()
                    val currentUrl =
                        currentWebView?.method { name = "getUrl"; superClass() }?.string() ?: ""
                    if (currentUrl.let { it.startsWith("http://m.weathercn.com") || it.startsWith("https://m.weathercn.com") }) {
                        val jsCommand = """
                          (function hideNewsElement() {
                            const elements = document.getElementsByClassName('news-con');
                            if (elements[0]) elements[0].hidden = true;
                            const element = document.getElementById('news-con');
                            if (element) element.hidden = true;
                          })();
                        """.trimIndent()
                        currentWebView?.method {
                            name = "evaluateJavascript"
                            paramCount = 2
                            superClass()
                        }?.call(jsCommand, null)
                    }
                }
            }
        }
    }
}