
package game.engine.reference


import effect.effApply
import effect.effError
import effect.effValue
import lulo.document.DocDict
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Data Reference
 */
@Suppress("UNCHECKED_CAST")
sealed class DataReference : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DataReference> = when (doc.case())
        {
            "data_reference_boolean"   -> DataReferenceBoolean.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<DataReference>
            "data_reference_dice_roll" -> DataReferenceDiceRoll.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<DataReference>
            "data_reference_number"    -> DataReferenceNumber.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<DataReference>
            else                        -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Boolean Value Reference
 */
data class DataReferenceBoolean(val reference : BooleanReference) : DataReference()
{

   companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DataReferenceBoolean> = when (doc)
        {
            is DocDict -> BooleanReference.fromSchemaDoc(doc) ap {
                                effValue<ValueError, DataReferenceBoolean>(
                                        DataReferenceBoolean(it))
                            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Dice Roll Value Reference
 */
data class DataReferenceDiceRoll(val reference : DiceRollReference) : DataReference()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DataReferenceDiceRoll> = when (doc)
        {
            is DocDict -> DiceRollReference.fromSchemaDoc(doc) ap {
                            effValue<ValueError, DataReferenceDiceRoll>(DataReferenceDiceRoll(it))
                          }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Number Value Reference
 */
data class DataReferenceNumber(val reference : NumberReference) : DataReference()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc: SchemaDoc): ValueParser<DataReferenceNumber> =
                effect.apply(::DataReferenceNumber, NumberReference.fromSchemaDoc(doc))
    }

}
