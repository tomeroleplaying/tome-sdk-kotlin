
package game.engine.variable


import effect.apply
import effect.effError
import game.engine.dice.DiceRoll
import lulo.document.DocDict
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Dice Variable Value
 */
sealed class DiceRollVariableValue : Serializable
{

    // -----------------------------------------------------------------------------------------
    // COSNTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceRollVariableValue> = when (doc)
        {
            is DocDict -> when (doc.case())
            {
                "literal" -> DiceRollVariableLiteralValue.fromSchemaDoc(doc)
                else      -> effError<ValueError, DiceRollVariableValue>(
                                UnknownCase(doc.case(), doc.path))
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Literal Value
 */
data class DiceRollVariableLiteralValue(val diceRoll : DiceRoll) : DiceRollVariableValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceRollVariableValue> =
            apply(::DiceRollVariableLiteralValue, DiceRoll.fromSchemaDoc(doc))
    }

}
