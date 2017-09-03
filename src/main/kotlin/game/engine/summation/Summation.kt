
package game.engine.summation


import effect.*
import game.engine.dice.*
import game.engine.variable.VariableReference
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import java.io.Serializable


/**
 * Summation
 */
data class Summation(val summationId : SummationId,
                     val summationName : String,
                     val terms : Set<SummationTerm>)
                      : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------


    companion object
    {
        fun fromSchemDoc(doc : SchemaDoc) : ValueParser<Summation> = when (doc)
        {
            is DocDict ->
            {
                apply(::Summation,
                      // Summation Id
                      doc.at("summation_id") ap { SummationId.fromSchemaDoc(it) },
                      // Summation Name
                      doc.text("summation_name"),
                      // Terms
                      doc.list("terms") ap {
                          it.mapSetMut { SummationTerm.fromDocument(it) }
                      })
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Summation Id
 */
data class SummationId(val value : String) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SummationId> = when (doc)
        {
            is DocText -> effValue(SummationId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }


}

