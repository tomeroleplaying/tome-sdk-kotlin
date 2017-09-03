
package theme


import effect.effError
import effect.effValue
import lulo.document.DocText
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser



/**
 * Color Id
 */
sealed class ColorId
{

    object Transparent : ColorId()


    object White : ColorId()


    object Black : ColorId()


    class Theme(val themeId : String) : ColorId()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ColorId> = when (doc)
        {
            is DocText ->
            {
                when (doc.text)
                {
                    "transparent" -> effValue<ValueError,ColorId>(ColorId.Transparent)
                    "white"       -> effValue<ValueError,ColorId>(ColorId.White)
                    "black"       -> effValue<ValueError,ColorId>(ColorId.Black)
                    else          -> effValue<ValueError,ColorId>(ColorId.Theme(doc.text))
                }
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}