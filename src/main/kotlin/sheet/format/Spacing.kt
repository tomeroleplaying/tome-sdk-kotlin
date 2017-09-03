
package sheet.format


import effect.apply
import effect.effError
import lulo.document.DocDict
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Spacing
 */
data class Spacing(val top : Double,
                   val right : Double,
                   val bottom : Double,
                   val left : Double) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Spacing> = when (doc)
        {
            is DocDict ->
            {
                apply(::Spacing,
                      // Top
                      doc.double("top"),
                      // Right
                      doc.double("top"),
                      // Bottom
                      doc.double("top"),
                      // Left
                      doc.double("top")
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = Spacing(0.0, 0.0, 0.0, 0.0)
    }

}
