
package game.engine.variable


import effect.apply
import effect.effError
import effect.effValue
import game.program.Invocation
import lulo.document.DocBoolean
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Boolean Variable
 */
sealed class BooleanVariableValue : Serializable
{

    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<BooleanVariableValue> = when (doc.case())
        {
            "boolean_literal"    -> BooleanVariableLiteralValue.fromDocument(doc)
            "program_invocation" -> BooleanVariableProgramValue.fromDocument(doc)
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Literal Value
 */
data class BooleanVariableLiteralValue(var value : Boolean) : BooleanVariableValue()
{

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<BooleanVariableValue> = when (doc)
        {
            is DocBoolean -> effValue(BooleanVariableLiteralValue(doc.boolean))
            else          -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Program Value
 */
data class BooleanVariableProgramValue(val invocation : Invocation) : BooleanVariableValue()
{

    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<BooleanVariableValue> =
                apply(::BooleanVariableProgramValue, Invocation.fromSchemaDoc(doc))
    }

}
