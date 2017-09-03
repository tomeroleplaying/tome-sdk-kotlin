package campaign


import effect.effError
import effect.effValue
import lulo.document.DocText
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import java.io.Serializable


/**
 * Campaign Id
 */
data class CampaignId(var value : String) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<CampaignId> = when (doc)
        {
            is DocText -> effValue(CampaignId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))

        }
    }
}