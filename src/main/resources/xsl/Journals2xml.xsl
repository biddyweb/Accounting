<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
	Journals
      </title>
    </head>
    <body>
      <h1>
        Journals
      </h1>
	<xsl:for-each select="Journals">
        <ul>
          <xsl:for-each select="Journal">
            <li>
              <xsl:element name="a">
                <xsl:attribute name="href">
                  <xsl:value-of select="xml"/>
                </xsl:attribute>
                  <xsl:value-of select="name"/>
              </xsl:element>
              (<xsl:value-of select="abbreviation"/> | <xsl:value-of select="type"/>)
            </li>
          </xsl:for-each>
        </ul>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>