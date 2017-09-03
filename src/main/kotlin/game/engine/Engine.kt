
package game.engine


import effect.apply
import effect.effApply
import effect.effError
import effect.effValue
import game.engine.dice.DiceRoll
import lulo.document.*
import lulo.schema.Prim
import lulo.value.*
import lulo.value.UnexpectedType
import java.io.Serializable



/**
 * Engine Value Type
 */
sealed class EngineValueType : Serializable
{

    object Number : EngineValueType()


    object Text : EngineValueType()


    object Boolean : EngineValueType()


    object DiceRoll : EngineValueType()


    object ListText : EngineValueType()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineValueType> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "number"    -> effValue<ValueError,EngineValueType>(EngineValueType.Number)
                "text"      -> effValue<ValueError,EngineValueType>(EngineValueType.Text)
                "boolean"   -> effValue<ValueError,EngineValueType>(EngineValueType.Boolean)
                "dice_roll" -> effValue<ValueError,EngineValueType>(EngineValueType.DiceRoll)
                "list_text" -> effValue<ValueError,EngineValueType>(EngineValueType.ListText)
                else        -> effError<ValueError,EngineValueType>(
                                 UnexpectedValue("EngineValueType", doc.text, doc.path))
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

    override fun toString(): String = when (this)
    {
        is Number   -> "Number"
        is Text     -> "Text"
        is Boolean  -> "Boolean"
        is DiceRoll -> "Dice Roll"
        is ListText -> "ListText"
    }

}




/**
 * Engine Value
 */
@Suppress("UNCHECKED_CAST")
sealed class EngineValue : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineValue> = when (doc.case())
        {
            "engine_value_number"  -> EngineValueNumber.fromSchemaDoc(doc)
                                        as ValueParser<EngineValue>
            "engine_value_text"    -> EngineValueText.fromSchemaDoc(doc)
                                        as ValueParser<EngineValue>
            "engine_value_boolean" -> EngineValueBoolean.fromSchemaDoc(doc)
                                        as ValueParser<EngineValue>
            "dice_roll"            -> EngineValueDiceRoll.fromSchemaDoc(doc)
                                        as ValueParser<EngineValue>
            "list_text"            -> EngineTextListValue.fromSchemaDoc(doc)
                                        as ValueParser<EngineValue>
            else                   -> effError(UnknownCase(doc.case(), doc.path))
        }
    }


    abstract fun type() : EngineValueType

}

/**
 * Engine Number Value
 */
data class EngineValueNumber(val value : Double) : EngineValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineValueNumber> = when (doc)
        {
            is DocNumber -> effValue(EngineValueNumber(doc.number))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // ENGINE VALUE
    // -----------------------------------------------------------------------------------------

    override fun type() = EngineValueType.Number

}


/**
 * Engine Text Value
 */
data class EngineValueText(val value : String) : EngineValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineValueText> = when (doc)
        {
            is DocText -> effValue(EngineValueText(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // ENGINE VALUE
    // -----------------------------------------------------------------------------------------

    override fun type() = EngineValueType.Text

}


/**
 * Engine Boolean Value
 */
data class EngineValueBoolean(val value : Boolean) : EngineValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineValueBoolean> = when (doc)
        {
            is DocBoolean -> effValue(EngineValueBoolean(doc.boolean))
            else          -> effError(UnexpectedType(DocType.BOOLEAN, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // ENGINE VALUE
    // -----------------------------------------------------------------------------------------

    override fun type() = EngineValueType.Boolean

}


/**
 * Engine Dice Roll Value
 */
data class EngineValueDiceRoll(val value : DiceRoll) : EngineValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<EngineValueDiceRoll> =
                apply(::EngineValueDiceRoll, DiceRoll.fromSchemaDoc(doc))
    }


    // -----------------------------------------------------------------------------------------
    // ENGINE VALUE
    // -----------------------------------------------------------------------------------------

    override fun type() = EngineValueType.DiceRoll

}

/**
 * Engine Text List Value
 */
data class EngineTextListValue(val value : List<String>) : EngineValue()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<EngineTextListValue> = when (doc)
        {
            is DocDict -> doc.list("value").apply {
                            apply(::EngineTextListValue, it.stringList()) }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // ENGINE VALUE
    // -----------------------------------------------------------------------------------------

    override fun type() = EngineValueType.ListText

}

