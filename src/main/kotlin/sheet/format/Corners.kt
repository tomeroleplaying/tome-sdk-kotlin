
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
 * Corners
 */
data class Corners(val topLeftRadius : Double,
                   val topRightRadius : Double,
                   val bottomRightRadius : Double,
                   val bottomLeftRadius : Double) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Corners> = when (doc)
        {
            is DocDict ->
            {
                apply(::Corners,
                      // Top
                      doc.double("top_left_radius"),
                      // Right
                      doc.double("top_right_radius"),
                      // Bottom
                      doc.double("bottom_right_radius"),
                      // Left
                      doc.double("bottom_left_radius")
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = Corners(0.0, 0.0, 0.0, 0.0)
    }

}