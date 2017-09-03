
package game.engine.reference


import effect.*
import game.engine.value.ValueReference
import game.engine.variable.VariableReference
import lulo.document.DocNumber
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Number Reference
 */
sealed class NumberReference : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberReference> = when (doc.case())
        {
            "number_literal"     -> NumberReferenceLiteral.fromSchemaDoc(doc.nextCase())
            "value_reference"    -> NumberReferenceValue.fromSchemaDoc(doc.nextCase())
            "variable_reference" -> NumberReferenceVariable.fromSchemaDoc(doc.nextCase())
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Literal Number Reference
 */
data class NumberReferenceLiteral(val value : Double) : NumberReference()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberReference> = when (doc)
        {
            is DocNumber -> effValue(NumberReferenceLiteral(doc.number))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}



/**
 * Value Number Reference
 */
data class NumberReferenceValue(val valueReference : ValueReference) : NumberReference()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<NumberReference> =
                apply(::NumberReferenceValue, ValueReference.fromSchemaDoc(doc))
    }

}


/**
 * Variable Number Reference
 */
data class NumberReferenceVariable(val variableReference : VariableReference)
                : NumberReference()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberReference> =
                apply(::NumberReferenceVariable, VariableReference.fromSchemaDoc(doc))
    }

}
