
package game.engine.variable


import effect.*
import game.engine.summation.SummationId
import game.engine.value.ValueReference
import game.program.Invocation
import jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
import lulo.document.*
import lulo.value.UnexpectedValue
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser



/**
 * Number Variable
 */
sealed class NumberVariableValue : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableValue> = when (doc.case())
        {
            "number_literal"     -> NumberVariableLiteralValue.fromDocument(doc)
            "variable_id"        -> NumberVariableVariableValue.fromSchemaDoc(doc)
            "program_invocation" -> NumberVariableProgramValue.fromDocument(doc)
            "value_reference"    -> NumberVariableValueValue.fromSchemaDoc(doc)
            "summation_id"       -> NumberVariableSummationValue.fromSchemaDoc(doc)
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Literal Value
 */
data class NumberVariableLiteralValue(val value : Double) : NumberVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<NumberVariableValue> = when (doc)
        {
            is DocNumber -> effValue(NumberVariableLiteralValue(doc.number))
            else         -> effError(lulo.value.UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}


/**
 * Unknown Literal Value
 */
class NumberVariableUnknownLiteralValue() : NumberVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableValue> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "unknown_literal_value" -> effValue<ValueError,NumberVariableValue>(
                                               NumberVariableUnknownLiteralValue())
                else                    -> effError<ValueError,NumberVariableValue>(
                                            UnexpectedValue("NumberVariableUnknownLiteralValue",
                                                    doc.text,
                                                    doc.path))
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Variable Value
 */
data class NumberVariableVariableValue(val variableId : VariableId) : NumberVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableValue> =
            apply(::NumberVariableVariableValue, VariableId.fromSchemaDoc(doc))
    }

}


/**
 * Program Value
 */
data class NumberVariableProgramValue(val invocation : Invocation) : NumberVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<NumberVariableValue> =
            apply(::NumberVariableProgramValue, Invocation.fromSchemaDoc(doc))
    }

}


/**
 * Program Value
 */
data class NumberVariableValueValue(val valueReference : ValueReference) : NumberVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableValue> =
            apply(::NumberVariableValueValue, ValueReference.fromSchemaDoc(doc))
    }

}


/**
 * Summation Value
 */
data class NumberVariableSummationValue(val summationId : SummationId) : NumberVariableValue()
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableValue> =
            apply(::NumberVariableSummationValue, SummationId.fromSchemaDoc(doc))
    }

}


// ---------------------------------------------------------------------------------------------
// HISTORY
// ---------------------------------------------------------------------------------------------

/**
 * Number Variable History
 */
data class NumberVariableHistory(val entries : List<NumberVariableHistoryEntry>)
                                  : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    constructor() : this(listOf())


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariableHistory> = when (doc)
        {
            is DocDict ->
            {
                apply(::NumberVariableHistory,
                      // Variable Id
                      doc.list("entries") ap {
                          it.mapMut { NumberVariableHistoryEntry.fromDocument(it) }
                      })
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}



/**
 * Number Variable History Entry
 */
data class NumberVariableHistoryEntry(val value : NumberVariableValue,
                                      val description : Maybe<String>)
                                       : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<NumberVariableHistoryEntry> = when (doc)
        {
            is DocDict ->
            {
                effApply(::NumberVariableHistoryEntry,
                        // Value
                        doc.at("value") ap { NumberVariableValue.fromSchemaDoc(it) },
                        // Description
                        split(doc.maybeText("description"),
                              effValue<ValueError,Maybe<String>>(Nothing()),
                              { effValue(Just(it)) })
                )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

