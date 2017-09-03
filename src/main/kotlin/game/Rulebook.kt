
package game


import effect.*
import lulo.document.*
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable
import java.util.*




// ---------------------------------------------------------------------------------------------
// CHAPTER
// --------------------------------------------------------------------------------------------

/**
 * Rulebook Chapter Id
 */
data class RulebookChapterId(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RulebookChapterId> = when (doc)
        {
            is DocText -> effValue(RulebookChapterId(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


// ---------------------------------------------------------------------------------------------
// SECTION
// --------------------------------------------------------------------------------------------

/**
 * Rulebook Subsection Id
 */
data class RulebookSectionId(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RulebookSectionId> = when (doc)
        {
            is DocText -> effValue(RulebookSectionId(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


// ---------------------------------------------------------------------------------------------
// SUBSECTION
// --------------------------------------------------------------------------------------------

/**
 * Rulebook Subsection Id
 */
data class RulebookSubsectionId(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RulebookSubsectionId> = when (doc)
        {
            is DocText -> effValue(RulebookSubsectionId(doc.text))
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


// ---------------------------------------------------------------------------------------------
// RULEBOOK REFERENCE
// --------------------------------------------------------------------------------------------

/**
 * Rulebook Reference
 */
data class RulebookReference(val chapterId : RulebookChapterId,
                             val sectionId : Maybe<RulebookSectionId>,
                             val subsectionId : Maybe<RulebookSubsectionId>)
                               : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<RulebookReference> = when (doc)
        {
            is DocDict ->
            {
                apply(::RulebookReference,
                      // Chapter Id
                      doc.at("chapter_id") apply { RulebookChapterId.fromSchemaDoc(it) },
                      // Section Id
                      split(doc.maybeAt("section_id"),
                            effValue<ValueError,Maybe<RulebookSectionId>>(Nothing()),
                            { effApply(::Just, RulebookSectionId.fromSchemaDoc(it)) }),
                      // Subsection Id
                      split(doc.maybeAt("subsection_id"),
                            effValue<ValueError,Maybe<RulebookSubsectionId>>(Nothing()),
                            { effApply(::Just, RulebookSubsectionId.fromSchemaDoc(it)) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

