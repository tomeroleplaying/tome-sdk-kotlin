
package game.engine.function


import effect.*
import game.engine.EngineValue
import game.engine.EngineValueType
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Function
 */
data class Function(val functionId : FunctionId,
                    val label : String,
                    val description : String,
                    val typeSignature : FunctionTypeSignature,
                    val tuples : List<Tuple>)
                     : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Function> = when (doc)
        {
            is DocDict ->
            {
                apply(::Function,
                        // Function Id
                        doc.at("function_id") ap { FunctionId.fromSchemaDoc(it) },
                        // Label
                        doc.text("label"),
                        // Description
                        doc.text("description"),
                        // Type Signature
                        doc.at("type_signature") ap { FunctionTypeSignature.fromSchemaDoc(it) },
                        // Tuples
                        doc.list("tuples") ap {
                            it.mapMut { Tuple.fromSchemaDoc(it) }
                        })
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Function Type Signature
 */
data class FunctionTypeSignature(val parameter1Type : EngineValueType,
                                 val parameter2Type : Maybe<EngineValueType>,
                                 val parameter3Type : Maybe<EngineValueType>,
                                 val parameter4Type : Maybe<EngineValueType>,
                                 val parameter5Type : Maybe<EngineValueType>,
                                 val resultType : EngineValueType)
                                   : java.io.Serializable
{


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<FunctionTypeSignature> = when (doc)
        {
            is DocDict ->
            {
                effApply(::FunctionTypeSignature,
                        // Parameter 1 Type
                        doc.at("parameter1_type") ap { EngineValueType.fromSchemaDoc(it) },
                        // Parameter 2 Type
                        split(doc.maybeAt("parameter2_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 3 Type
                        split(doc.maybeAt("parameter3_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 4 Type
                        split(doc.maybeAt("parameter4_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 5 Type
                        split(doc.maybeAt("parameter5_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Result Type
                        doc.at("result_type") ap { EngineValueType.fromSchemaDoc(it) }
                )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }

    }

}


/**
 * Function Id
 */
data class FunctionId(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<FunctionId> = when (doc)
        {
            is DocText -> effValue(FunctionId(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}



/**
 * Tuple
 */
data class Tuple(val parameter1 : EngineValue,
                 val parameter2 : Maybe<EngineValue>,
                 val parameter3 : Maybe<EngineValue>,
                 val parameter4 : Maybe<EngineValue>,
                 val parameter5 : Maybe<EngineValue>,
                 val result : EngineValue)
                   : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Tuple> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::Tuple,
                        // Parameter 1
                        doc.at("parameter1") ap { EngineValue.fromSchemaDoc(it) },
                        // Parameter 2
                        split(doc.maybeAt("parameter2"),
                                effValue<ValueError,Maybe<EngineValue>>(Nothing()),
                                { effApply(::Just, EngineValue.fromSchemaDoc(it)) }),
                        // Parameter 3
                        split(doc.maybeAt("parameter3"),
                                effValue<ValueError,Maybe<EngineValue>>(Nothing()),
                                { effApply(::Just, EngineValue.fromSchemaDoc(it)) }),
                        // Parameter 4
                        split(doc.maybeAt("parameter4"),
                                effValue<ValueError,Maybe<EngineValue>>(Nothing()),
                                { effApply(::Just, EngineValue.fromSchemaDoc(it)) }),
                        // Parameter 5
                        split(doc.maybeAt("parameter5"),
                                effValue<ValueError,Maybe<EngineValue>>(Nothing()),
                                { effApply(::Just, EngineValue.fromSchemaDoc(it)) }),
                        // Result
                        doc.at("result") ap { EngineValue.fromSchemaDoc(it) }
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

