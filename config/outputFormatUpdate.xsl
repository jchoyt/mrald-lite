<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="OutputFormatUpdate">
    <!-- Start Output Size and Format Selections -->
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="bord">
          <table width="100%" border="0" cellpadding="3" cellspacing="1">
            <tr>
              <th>
                Output Size and Format Selections
              </th>
            </tr>
            <tr>
              <td>
                <table width="100%">
                  <tr>
                    <td>
                      <strong>
                        Output size limit
                      </strong>
                      <br />
                      <input type="radio" value="none" name="outputSize">None</input>
                      <input type="radio" value="lines" name="outputSize" checked="" />
                      <input name="outputLinesCount" type="text" size="6" value="500">Lines</input>
                      <input type="radio" value="mb" name="outputSize" />
                      <input name="outputMBSize" type="text" size="5" value="1">MB</input>
                    </td>
                    <td colspan="2" valign="top">
                      <input type="checkbox" name="showDuplicates" value="true">Include duplicate rows in returned data</input>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3">
                      <hr />
                      <strong>
                        Format and Destination
                      </strong>
                    </td>
                  </tr>
                  <tr>
                    <td width="25%" valign="top">
                      <strong>
                        Browser
                      </strong>
                      <br />
                      <input type="radio" name="Format" value="updateListHtml" checked="">Update Table (HTML)</input>
                    </td>

                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <br />
    <!-- End Output Size and Format Selections -->
  </xsl:template>


</xsl:stylesheet>


