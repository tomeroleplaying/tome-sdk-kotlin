
package sheet.format


import effect.effError
import effect.effValue
import lulo.document.DocText
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.UnexpectedValue
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



sealed class Position : Serializable
{

    object Left : Position()


    object Top : Position()


    object Right : Position()


    object Bottom : Position()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Position> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "left"   -> effValue<ValueError,Position>(Position.Left)
                "top"    -> effValue<ValueError,Position>(Position.Top)
                "right"  -> effValue<ValueError,Position>(Position.Right)
                "bottom" -> effValue<ValueError,Position>(Position.Bottom)
                else     -> effError<ValueError,Position>(
                        UnexpectedValue("Corners", doc.text, doc.path))
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}

