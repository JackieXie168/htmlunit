<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="faqs">
    <s1 title="{@title}">
      <s2 title="Questions">
      	<br/>
        <xsl:apply-templates select="group" mode="index"/>
        <ul>
          <xsl:apply-templates select="faq" mode="index"/>
        </ul>
      </s2>
      <s2 title="Answers">
      	<br/>
        <xsl:apply-templates select="group/faq"/>
        <br/>
        <xsl:apply-templates select="faq"/>
      </s2>
    </s1>
  </xsl:template>

  <xsl:template match="group" mode="index">
    <xsl:value-of select="@title"/>
    <ul>
      <xsl:apply-templates select="faq" mode="index"/>
    </ul>
  </xsl:template>

  <xsl:template match="faq" mode="index">
    <li>
      <link anchor="faq-{generate-id(.)}">
        <xsl:if test="string-length(@title)=0">
          <xsl:value-of select="q"/>
        </xsl:if>
        <xsl:if test="string-length(@title)>0">
          <xsl:value-of select="@title"/>
        </xsl:if>
      </link>
    </li>
  </xsl:template>

  <xsl:template match="faq">
    <anchor name="faq-{generate-id(.)}"/>
    <s3 title="{q}">
      <br/>
      <xsl:apply-templates select="a"/>
    </s3>
  </xsl:template>

  <xsl:template match="a">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
