
package game.engine.value


import effect.*
import game.RulebookReference
import game.engine.variable.Variable
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Value
 */
@Suppress("UNCHECKED_CAST")
sealed class Value(open val valueId : ValueId,
                   open val description : Maybe<String>,
                   open val rulebookReference : Maybe<RulebookReference>,
                   open val variables : MutableSet<Variable>,
                   open val valueSetId : ValueSetId)
                    : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc, valueSetId : ValueSetId) : ValueParser<Value> =
            when (doc)
            {
                is DocDict ->
                {
                    when (doc.case())
                    {
                        "value_number" -> ValueNumber.fromSchemaDoc(doc, valueSetId) as ValueParser<Value>
                        "value_text"   -> ValueText.fromSchemaDoc(doc, valueSetId) as ValueParser<Value>
                        else           -> effError<ValueError, Value>(
                                UnknownCase(doc.case(), doc.path))
                    }
                }
                else -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
            }
    }

}


/**
 * Number Value
 */
data class ValueNumber(override val valueId : ValueId,
                       override val description: Maybe<String>,
                       override val rulebookReference : Maybe<RulebookReference>,
                       override val variables : MutableSet<Variable>,
                       override val valueSetId : ValueSetId,
                       val value : Double)
                        : Value(valueId, description, rulebookReference, variables, valueSetId)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc,
                          valueSetId : ValueSetId) : ValueParser<ValueNumber> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::ValueNumber,
                      // Value Id
                      doc.at("value_id") ap { ValueId.fromSchemaDoc(it) },
                      // Description
                      split(doc.maybeText("description"),
                              effValue<ValueError,Maybe<String>>(Nothing()),
                              { effValue(Just(it)) }),
                      // Rulebook Reference
                      split(doc.maybeAt("rulebook_reference"),
                              effValue<ValueError,Maybe<RulebookReference>>(Nothing()),
                              { effApply(::Just, RulebookReference.fromSchemaDoc(it)) }),
                      // Variables
                      doc.list("variables") ap { docList ->
                          docList.mapSetMut { Variable.fromSchemaDoc(it) }
                      },
                      // Value Set Id
                      effValue(valueSetId),
                      // Value
                      doc.double("value")
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // EQUALS
    // -----------------------------------------------------------------------------------------
//
//    override fun equals(other : Any?) : Boolean =
//            if (other is ValueNumber)
//                other.value() == this.value()
//            else
//                false

}


/**
 * Text Value
 */
data class ValueText(override val valueId : ValueId,
                     override val description : Maybe<String>,
                     override val rulebookReference : Maybe<RulebookReference>,
                     override val variables : MutableSet<Variable>,
                     override val valueSetId : ValueSetId,
                     val value : String)
                       : Value(valueId, description, rulebookReference, variables, valueSetId)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc,
                          valueSetId : ValueSetId) : ValueParser<ValueText> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::ValueText,
                      // Value Id
                      doc.at("value_id") ap { ValueId.fromSchemaDoc(it) },
                      // Description
                      split(doc.maybeText("description"),
                              effValue<ValueError,Maybe<String>>(Nothing()),
                              { effValue(Just(it)) }),
                      // Rulebook Reference
                      split(doc.maybeAt("rulebook_reference"),
                              effValue<ValueError,Maybe<RulebookReference>>(Nothing()),
                              { effApply(::Just, RulebookReference.fromSchemaDoc(it)) }),
                      // Variables
                      split(doc.maybeList("variables"),
                              effValue(mutableSetOf()),
                              { it.mapSetMut { Variable.fromSchemaDoc(it) } }),
                      // Value Set Id
                      effValue(valueSetId),
                      // Value
                      doc.text("value")
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // EQUALS
    // -----------------------------------------------------------------------------------------

//    override fun equals(other : Any?) : Boolean =
//            if (other is ValueText)
//                other.value() == this.value()
//            else
//                false
//
//
//    override fun hashCode(): Int {
//        return super.hashCode()
//    }
}




/**
 * Value Id
 */
data class ValueId(val value : String) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<ValueId> = when (doc)
        {
            is DocText -> effValue(ValueId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }


}


/**
 * Value Reference
 */
data class ValueReference(val valueSetId : ValueSetId, val valueId : ValueId) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ValueReference> = when (doc)
        {
            is DocDict ->
            {
                apply(::ValueReference,
                      // Value Set Name
                      doc.at("value_set_id") ap { ValueSetId.fromSchemaDoc(it) },
                      // Value Name
                      doc.at("value_id") ap { ValueId.fromSchemaDoc(it) })
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}
