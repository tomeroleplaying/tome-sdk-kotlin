
package sheet.format


import effect.effError
import effect.effValue
import lulo.document.DocNumber
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Height
 */
sealed class Height : Serializable
{


    object Wrap : Height()


    data class Fixed(val value : Float) : Height()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Height> = when (doc)
        {
            is DocNumber ->
            {
                val num = doc.number.toFloat()
                if (num == 0.0f)
                    effValue<ValueError,Height>(Height.Wrap)
                else
                    effValue<ValueError,Height>(Height.Fixed(num))
            }
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }


}
