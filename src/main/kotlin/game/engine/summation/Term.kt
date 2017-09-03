
package game.engine.summation


import effect.*
import game.engine.reference.BooleanReference
import game.engine.reference.DiceRollReference
import game.engine.reference.NumberReference
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
 * Summation Term
 */
sealed class SummationTerm(open val termName : Maybe<String>) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SummationTerm> = when (doc.case())
        {
            "summation_term_number"      -> SummationTermNumber.fromSchemaDoc(doc)
            "summation_term_dice_roll"   -> SummationTermDiceRoll.fromSchemaDoc(doc)
            "summation_term_conditional" -> SummationTermConditional.fromSchemaDoc(doc)
            else                         -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


data class SummationTermNumber(override val termName : Maybe<String>,
                               val numberReference : NumberReference)
                                : SummationTerm(termName)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SummationTerm> = when (doc)
        {
            is DocDict ->
            {
                apply(::SummationTermNumber,
                      // Term Name
                      split(doc.maybeText("term_name"),
                            effValue<ValueError,Maybe<String>>(Nothing()),
                            { effValue(Just(it)) }),
                      // Value
                      doc.at("value") ap { NumberReference.fromSchemaDoc(it) })
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class SummationTermDiceRoll(override val termName : Maybe<String>,
                                 val diceRollReference : DiceRollReference)
                                  : SummationTerm(termName)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SummationTerm> = when (doc)
        {
            is DocDict ->
            {
                apply(::SummationTermDiceRoll,
                      // Term Name
                      split(doc.maybeText("term_name"),
                            effValue<ValueError,Maybe<String>>(Nothing()),
                            { effValue(Just(it)) }),
                      // Value
                      doc.at("value") ap { DiceRollReference.fromSchemaDoc(it) }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class SummationTermConditional(override val termName : Maybe<String>,
                                    val conditionalValueReference : BooleanReference,
                                    val trueValueReference : NumberReference,
                                    val falseValueReference: NumberReference)
                                     : SummationTerm(termName)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SummationTerm> = when (doc)
        {
            is DocDict ->
            {
                apply(::SummationTermConditional,
                      // Term Name
                      split(doc.maybeText("term_name"),
                              effValue<ValueError,Maybe<String>>(Nothing()),
                              { effValue(Just(it)) }),
                      // Condition
                      doc.at("condition") ap { BooleanReference.fromSchemaDoc(it) },
                      // When True
                      doc.at("when_true") ap { NumberReference.fromSchemaDoc(it) },
                      // When False
                      doc.at("when_false") ap { NumberReference.fromSchemaDoc(it) })
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

