
package sheet


import campaign.CampaignId
import com.sun.org.apache.xpath.internal.operations.Variable
import effect.effError
import effect.effValue
import effect.split
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Sheet
 */
data class Sheet(var sheetId : SheetId,
                 var campaignId : CampaignId,
                 val sections : MutableList<Section>,
                 val variables : MutableSet<Variable>,
                 val settings : Settings) : java.io.Serializable
{


    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<Sheet> = when (doc)
        {
            is DocDict ->
            {
                apply(::Sheet,
                        // Sheet Id
                        doc.at("id").apply { SheetId.fromDocument(it) },
                        // Campaign Id
                        doc.at("campaign_id") ap { CampaignId.fromSchemaDoc(it) },
                        // Section List
                        doc.list("sections") ap { docList ->
                            docList.mapMut { Section.fromSchemaDoc(it) }
                        },
                        // Variables
                        split(doc.maybeList("variables"),
                                effValue<ValueError,MutableSet<Variable>>(mutableSetOf()),
                                { it.mapSetMut { Variable.fromDocument(it) } }),
                        // Sheet Settings
                        split(doc.maybeAt("settings"),
                                effValue(Settings.default()),
                                { Settings.fromDocument(it) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class SheetId(val value : String) : Serializable
{

    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<SheetId> = when (doc)
        {
            is DocText -> effValue(SheetId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}



