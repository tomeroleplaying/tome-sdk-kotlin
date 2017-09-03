
package game.engine.dice


import effect.*
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Dice Roll
 */
data class DiceRoll(val quantities : Set<DiceQuantity>,
                    val modifiers : Set<RollModifier>,
                    val rollName : Maybe<DiceRollName>)
                     : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceRoll> = when (doc)
        {
            is DocDict ->
            {
                apply(::DiceRoll,
                      // Quantity
                      doc.list("quantities") ap { docList ->
                          docList.mapSetMut { DiceQuantity.fromSchemaDoc(it) }
                      },
                      // Modifier
                      split(doc.maybeList("modiiers"),
                              effValue(mutableSetOf<RollModifier>()),
                              { it.mapSetMut { RollModifier.fromSchemaDoc(it) } }),
                      // Name
                      split(doc.maybeAt("name"),
                              effValue<ValueError,Maybe<DiceRollName>>(Nothing()),
                              { effApply(::Just, DiceRollName.fromSchemaDoc(it))}
                      ))
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // TO STRING
    // -----------------------------------------------------------------------------------------
//
//    override fun toString() : String
//    {
//        val diceString = this.quantities().sortedBy { it.sidesInt() }
//                .map { it.toString() }
//                .joinToString(" + ")
//
//        val modifierSum = this.modifierValues().sum()
//        var modifierString = ""
//        if (modifierSum != 0)
//            modifierString = " + " + modifierSum.toString()
//
//        return diceString + modifierString
//    }

}


/**
 * Dice Quantity
 */
data class DiceQuantity(val sides : DiceSides,
                        val quantity : DiceRollQuantity)
                         : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceQuantity> = when (doc)
        {
            is DocDict ->
            {
                apply(::DiceQuantity,
                      // Sides
                      doc.at("sides") ap { DiceSides.fromSchemaDoc(it) },
                      // Quantity
                      doc.at("quantity") ap { DiceRollQuantity.fromSchemaDoc(it)  }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Dice Roll Name
 */
data class DiceRollName(val value : String) : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<DiceRollName> = when (doc)
        {
            is DocText -> effValue(DiceRollName(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Dice Sides
 */
data class DiceSides(val value : Int) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceSides> = when (doc)
        {
            is DocNumber -> effValue(DiceSides(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }


}


/**
 * Dice Roll Quantity
 */
data class DiceRollQuantity(val value : Int) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceRollQuantity> = when (doc)
        {
            is DocNumber -> effValue(DiceRollQuantity(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}


/**
 * Dice Modifier
 */
data class RollModifier(val value : RollModifierValue,
                        val modifierName : Maybe<RollModifierName>)
                         : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RollModifier> = when (doc)
        {
            is DocDict ->
            {
                apply(::RollModifier,
                      // Value
                      doc.at("value") ap { RollModifierValue.fromSchemaDoc(it) },
                      // Name
                      split(doc.maybeAt("name"),
                            effValue<ValueError,Maybe<RollModifierName>>(Nothing()),
                            { effApply(::Just, RollModifierName.fromSchemaDoc(it)) } )
            )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Roll Modifier Value
 */
data class RollModifierValue(val value : Double) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RollModifierValue> = when (doc)
        {
            is DocNumber -> effValue(RollModifierValue(doc.number))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}


/**
 * Roll Modifier Name
 */
data class RollModifierName(val value : String) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RollModifierName> = when (doc)
        {
            is DocText -> effValue(RollModifierName(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}
