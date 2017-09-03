
package game.engine.reference


import effect.effApply
import effect.effError
import effect.effValue
import game.engine.variable.VariableReference
import lulo.document.DocBoolean
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Boolean Reference
 */
sealed class BooleanReference : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<BooleanReference> = when (doc.case())
        {
            "literal"  -> BooleanReferenceLiteral.fromDocument(doc)
            "variable" -> BooleanReferenceVariable.fromDocument(doc)
            else       -> effError(UnknownCase(doc.case(), doc.path))
        }
    }


    // -----------------------------------------------------------------------------------------
    // DEPENDENCIES
    // -----------------------------------------------------------------------------------------

    open fun dependencies(): Set<VariableReference> = setOf()

}


/**
 * Literal Boolean Reference
 */
data class BooleanReferenceLiteral(val value : Boolean) : BooleanReference()
{

    companion object
    {
        fun fromDocument(doc: SchemaDoc): ValueParser<BooleanReference> = when (doc)
        {
            is DocBoolean -> effValue(BooleanReferenceLiteral(doc.boolean))
            else          -> effError(UnexpectedType(DocType.BOOLEAN, docType(doc), doc.path))
        }
    }

}


/**
 * Variable Boolean Reference
 */
data class BooleanReferenceVariable(val variableReference : VariableReference)
                    : BooleanReference()
{

    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<BooleanReference> =
                effApply(::BooleanReferenceVariable, VariableReference.fromSchemaDoc(doc))
    }

}
