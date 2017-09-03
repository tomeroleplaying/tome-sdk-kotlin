
package game.engine.variable


import effect.*
import game.engine.value.ValueReference
import game.engine.value.ValueSetId
import game.program.Invocation
import lulo.document.DocText
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.*
import java.io.Serializable



/**
 * Text Variable Value
 */
sealed class TextVariableValue : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariableValue> = when (doc.case())
        {
            "text_literal"       -> TextVariableLiteralValue.fromSchemaDoc(doc)
            "value_reference"    -> TextVariableValueValue.fromSchemaDoc(doc)
            "program_invocation" -> TextVariableProgramValue.fromSchemaDoc(doc)
            "value_set_id"       -> TextVariableValueUnknownValue.fromDocument(doc)
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Literal Value
 */
data class TextVariableLiteralValue(val value : String) : TextVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariableValue> = when (doc)
        {
            is DocText -> effValue(TextVariableLiteralValue(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Unknown Literal Value
 */
class TextVariableUnknownLiteralValue() : TextVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariableValue> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "unknown_literal_value" -> effValue<ValueError,TextVariableValue>(
                                                TextVariableUnknownLiteralValue())
                else                    -> effError<ValueError,TextVariableValue>(
                                                UnexpectedValue("TextVariableUnknownLiteralValue",
                                                        doc.text,
                                                        doc.path))
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


data class TextVariableValueValue(val valueReference : ValueReference) : TextVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariableValue> =
            apply(::TextVariableValueValue, ValueReference.fromSchemaDoc(doc))
    }

}


data class TextVariableValueUnknownValue(val valueSetId : ValueSetId) : TextVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<TextVariableValue> =
            apply(::TextVariableValueUnknownValue, ValueSetId.fromSchemaDoc(doc))
    }

}


data class TextVariableProgramValue(val invocation : Invocation) : TextVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariableValue> =
            apply(::TextVariableProgramValue, Invocation.fromSchemaDoc(doc))
    }

}

