
package game.engine.variable


import effect.*
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser



@Suppress("UNCHECKED_CAST")
sealed class Variable(open var variableId : VariableId,
                      open var label : String,
                      open var description : String,
                      open val tags : MutableSet<VariableTag>)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Variable> = when (doc.case())
        {
            "variable_boolean"   -> BooleanVariable.fromSchemaDoc(doc) as ValueParser<Variable>
            "variable_dice_roll" -> DiceRollVariable.fromSchemaDoc(doc) as ValueParser<Variable>
            "variable_number"    -> NumberVariable.fromSchemaDoc(doc) as ValueParser<Variable>
            "variable_text"      -> TextVariable.fromSchemaDoc(doc) as ValueParser<Variable>
            else                 -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Boolean Variable
 */
data class BooleanVariable(override var variableId : VariableId,
                           override var label : String,
                           override var description : String,
                           override val tags : MutableSet<VariableTag>,
                           var variableValue : BooleanVariableValue)
                            : Variable(variableId, label, description, tags)
{


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<BooleanVariable> = when (doc)
        {
            is DocDict ->
            {
                apply(::BooleanVariable,
                      // Variable Id
                      doc.at("id") ap { VariableId.fromSchemaDoc(it) },
                      // Label
                      doc.text("label"),
                      // Description
                      doc.text("description"),
                      // Tags
                      split(doc.maybeList("tags"),
                            effValue(mutableSetOf()),
                            { it.mapSetMut { VariableTag.fromSchemaDoc(it) }}),
                      // Value
                      doc.at("value") ap { BooleanVariableValue.fromDocument(it) }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Dice Variable
 */
data class DiceRollVariable(override var variableId : VariableId,
                            override var label : String,
                            override var description : String,
                            override val tags : MutableSet<VariableTag>,
                            val variableValue : DiceRollVariableValue)
                             : Variable(variableId, label, description, tags)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DiceRollVariable> = when (doc)
        {
            is DocDict ->
            {
                apply(::DiceRollVariable,
                      // Variable Id
                      doc.at("id") ap { VariableId.fromSchemaDoc(it) },
                      // Label
                      doc.text("label"),
                      // Description
                      doc.text("description"),
                      // Tags
                      split(doc.maybeList("tags"),
                            effValue(mutableSetOf()),
                            { it.mapSetMut { VariableTag.fromSchemaDoc(it) }}),
                      // Value
                      doc.at("value") ap { DiceRollVariableValue.fromSchemaDoc(it) }
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}



/**
 * Number Variable
 */
data class NumberVariable(override var variableId : VariableId,
                          override var label : String,
                          override var description : String,
                          override val tags : MutableSet<VariableTag>,
                          val variableValue : NumberVariableValue,
                          val history : NumberVariableHistory)
                           : Variable(variableId, label, description, tags)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<NumberVariable> = when (doc)
        {
            is DocDict ->
            {
                apply(::NumberVariable,
                      // Variable Id
                      doc.at("id") ap { VariableId.fromSchemaDoc(it) },
                      // Label
                      doc.text("label"),
                      // Description
                      doc.text("description"),
                      // Tags
                      split(doc.maybeList("tags"),
                            effValue(mutableSetOf()),
                            { it.mapSetMut { VariableTag.fromSchemaDoc(it) }}),
                      // Value
                      doc.at("value") ap { NumberVariableValue.fromSchemaDoc(it) },
                      // History
                      split(doc.maybeAt("history"),
                            effValue(NumberVariableHistory()),
                            { NumberVariableHistory.fromSchemaDoc(it) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


}


/**
 * Text Variable
 */
data class TextVariable(override var variableId : VariableId,
                        override var label : String,
                        override var description : String,
                        override val tags : MutableSet<VariableTag>,
                        var variableValue : TextVariableValue)
                         : Variable(variableId, label, description, tags)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextVariable> = when (doc)
        {
            is DocDict ->
            {
                apply(::TextVariable,
                      // Variable Id
                      doc.at("id") ap { VariableId.fromSchemaDoc(it) },
                      // Label
                      doc.text("label"),
                      // Description
                      doc.text("description"),
                      // Tags
                      split(doc.maybeList("tags"),
                            effValue(mutableSetOf()),
                            { it.mapSetMut { VariableTag.fromSchemaDoc(it) }}),
                      // Value
                      doc.at("value") ap { TextVariableValue.fromSchemaDoc(it) }
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}




/**
 * Variable Reference
 */
@Suppress("UNCHECKED_CAST")
sealed class VariableReference : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VariableReference> =
            when (doc.case())
            {
                "variable_id"  -> VariableId.fromSchemaDoc(doc) as ValueParser<VariableReference>
                "variable_tag" -> VariableTag.fromSchemaDoc(doc) as ValueParser<VariableReference>
                else           -> effError(UnknownCase(doc.case(), doc.path))
            }
    }
}


/**
 * Variable Id
 */
data class VariableId(val namespace : Maybe<VariableNamespace>,
                      val name : VariableName) : java.io.Serializable
{


    constructor(name : VariableName) : this(Nothing(), name)


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VariableId> = when (doc)
        {
            is DocDict ->
            {
                apply(::VariableId,
                        // Namespace
                        split(doc.maybeAt("namespace"),
                              effValue<ValueError,Maybe<VariableNamespace>>(Nothing()),
                              { apply(::Just, VariableNamespace.fromSchemaDoc(it))  }),
                        // Name
                        doc.at("name") ap { VariableName.fromSchemaDoc(it) }
                )
            }
            is DocText -> apply(::VariableId, effValue(VariableName(doc.text)))
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Variable Tag
 */
data class VariableTag(val value : String) : VariableReference()
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VariableTag> = when (doc)
        {
            is DocText -> effValue(VariableTag(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}




/**
 * Variable Namespace
 */
data class VariableNamespace(val value : String) : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VariableNamespace> = when (doc)
        {
            is DocText -> effValue(VariableNamespace(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Variable Name
 */
data class VariableName(val value : String) : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VariableName> = when (doc)
        {
            is DocText -> effValue(VariableName(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}

