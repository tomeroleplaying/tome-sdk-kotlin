
package game.engine.value


import effect.*
import game.engine.EngineValueType
import lulo.document.*
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable
import java.util.*


/**
 * Value Set
 */
@Suppress("UNCHECKED_CAST")
sealed class ValueSet(open val valueSetId : ValueSetId,
                      open val label : String,
                      open val labelSingular : String,
                      open val description : String,
                      open val valueType : Maybe<EngineValueType>)
                       : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ValueSet> = when (doc)
        {
            is DocDict -> when (doc.case())
            {
                "value_set_base"     -> ValueSetBase.fromSchemaDoc(doc)
                                            as ValueParser<ValueSet>
                "value_set_compound" -> ValueSetCompound.fromSchemaDoc(doc)
                                            as ValueParser<ValueSet>
                else                 -> effError<ValueError,ValueSet>(
                                            UnknownCase(doc.case(), doc.path))
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Base Value Set
 */
data class ValueSetBase(override val valueSetId : ValueSetId,
                        override val label : String,
                        override val labelSingular : String,
                        override val description : String,
                        override val valueType : Maybe<EngineValueType>,
                        val values : MutableSet<Value>)
                         : ValueSet(valueSetId, label, labelSingular, description, valueType)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ValueSetBase> = when (doc)
        {
            is DocDict ->
            {
                doc.at("value_set_id") ap { ValueSetId.fromSchemaDoc(it) } ap { valueSetId ->
                    apply(::ValueSetBase,
                          // Value Set Id
                          effValue(valueSetId),
                          // Label
                          doc.text("label"),
                          // Label Singular
                          doc.text("label_singular"),
                          // Description
                          doc.text("description"),
                          // Value Type
                          split(doc.maybeAt("value_type"),
                                  effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                  { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                          // Values,
                          doc.list("values") ap {
                              it.mapSetMut { Value.fromSchemaDoc(it, valueSetId) }
                          })
                }
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


}


/**
 * Compound Value Set
 */
data class ValueSetCompound(override val valueSetId : ValueSetId,
                            override val label : String,
                            override val labelSingular : String,
                            override val description : String,
                            override val valueType : Maybe<EngineValueType>,
                            val valueSetIds : MutableSet<ValueSetId>)
                             : ValueSet(valueSetId, label, labelSingular, description, valueType)
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ValueSetCompound> = when (doc)
        {
            is DocDict ->
            {
                apply(::ValueSetCompound,
                      // Value Set Id
                      doc.at("value_set_id") ap { ValueSetId.fromSchemaDoc(it) },
                      // Label
                      doc.text("label"),
                      // Label Singular
                      doc.text("label_singular"),
                      // Description
                      doc.text("description"),
                      // Value Type
                      split(doc.maybeAt("value_type"),
                              effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                              { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                      // Value Set Ids
                      doc.list("value_set_ids") ap {
                          it.mapSetMut { ValueSetId.fromSchemaDoc(it) }
                      })
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * ValueSet Id
 */
data class ValueSetId(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ValueSetId> = when (doc)
        {
            is DocText -> effValue(ValueSetId(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}

