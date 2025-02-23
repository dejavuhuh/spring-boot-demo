@file:Suppress("ktlint")
package gg.jte.generated.ondemand.mail
import kiss.mail.Page
@Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")
class JteindexGenerated {
companion object {
	@JvmField val JTE_NAME = "mail/index.kte"
	@JvmField val JTE_LINE_INFO = intArrayOf(0,0,0,2,2,2,2,2,5,5,5,6,6,6,6,6,6,6,6,6,7,7,8,8,8,11,11,11,13,13,13,2,2,2,2,2)
	@JvmStatic fun render(jteOutput:gg.jte.html.HtmlTemplateOutput, jteHtmlInterceptor:gg.jte.html.HtmlInterceptor?, page:Page) {
		jteOutput.writeContent("\n<head>\n    ")
		if (page.description != null) {
			jteOutput.writeContent("\n        <meta name=\"description\"")
			val __jte_html_attribute_0 = page.description
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" content=\"")
				jteOutput.setContext("meta", "content")
				jteOutput.writeUserContent(__jte_html_attribute_0)
				jteOutput.setContext("meta", null)
				jteOutput.writeContent("\"")
			}
			jteOutput.writeContent(">\n    ")
		}
		jteOutput.writeContent("\n    <title>")
		jteOutput.setContext("title", null)
		jteOutput.writeUserContent(page.title)
		jteOutput.writeContent("</title>\n</head>\n<body>\n<h1>")
		jteOutput.setContext("h1", null)
		jteOutput.writeUserContent(page.title)
		jteOutput.writeContent("</h1>\n<p>Welcome to my example page!</p>\n</body>")
	}
	@JvmStatic fun renderMap(jteOutput:gg.jte.html.HtmlTemplateOutput, jteHtmlInterceptor:gg.jte.html.HtmlInterceptor?, params:Map<String, Any?>) {
		val page = params["page"] as Page
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
}
