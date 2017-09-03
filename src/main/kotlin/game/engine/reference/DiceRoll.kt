
package game.engine.reference


import effect.effApply
import effect.effError
import game.engine.dice.DiceRoll
import game.engine.variable.VariableReference
import lulo.document.SchemaDoc
import lulo.value.UnknownCase
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Dice Roll Reference
 */
sealed class DiceRollReference : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<DiceRollReference> = when (doc.case())
        {
            "dice_roll"          -> DiceRollReferenceLiteral.fromDocument(doc)
            "variable_reference" -> DiceRollReferenceVariable.fromDocument(doc)
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Literal Dice Roll Reference
 */
data class DiceRollReferenceLiteral(val value : DiceRoll) : DiceRollReference()
{

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<DiceRollReference> =
                effApply(::DiceRollReferenceLiteral, DiceRoll.fromSchemaDoc(doc))

    }

}


/**
 * Variable Dice Roll Reference
 */
data class DiceRollReferenceVariable(val variableReference : VariableReference)
                    : DiceRollReference()
{

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<DiceRollReference> =
                effApply(::DiceRollReferenceVariable, VariableReference.fromSchemaDoc(doc))

    }
}
