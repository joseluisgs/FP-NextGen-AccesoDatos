package es.joseluisgs.kotlinspringbootrestservice.utils.pagination

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class PaginationLinks {
    fun createLinkHeader(page: Page<*>, uriBuilder: UriComponentsBuilder): String {
        val linkHeader = StringBuilder()
        linkHeader.append("")
        if (page.hasNext()) {
            val uri = constructUri(page.number + 1, page.size, uriBuilder)
            linkHeader.append(buildLinkHeader(uri, "next"))
        }
        if (page.hasPrevious()) {
            val uri = constructUri(page.number - 1, page.size, uriBuilder)
            appendCommaIfNecessary(linkHeader)
            linkHeader.append(buildLinkHeader(uri, "prev"))
        }
        if (!page.isFirst) {
            val uri = constructUri(0, page.size, uriBuilder)
            appendCommaIfNecessary(linkHeader)
            linkHeader.append(buildLinkHeader(uri, "first"))
        }
        if (!page.isLast) {
            val uri = constructUri(page.totalPages - 1, page.size, uriBuilder)
            appendCommaIfNecessary(linkHeader)
            linkHeader.append(buildLinkHeader(uri, "last"))
        }
        return linkHeader.toString()
    }

    private fun constructUri(newPageNumber: Int, size: Int, uriBuilder: UriComponentsBuilder): String {
        return uriBuilder.replaceQueryParam("page", newPageNumber).replaceQueryParam("size", size).build().encode()
            .toUriString()
    }

    private fun buildLinkHeader(uri: String, rel: String): String {
        return "<$uri>; rel=\"$rel\""
    }

    private fun appendCommaIfNecessary(linkHeader: StringBuilder) {
        if (linkHeader.length > 0) {
            linkHeader.append(", ")
        }
    }
}