package com.jana.bms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Language} entity. This class is used
 * in {@link com.jana.bms.web.rest.LanguageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /languages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class LanguageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter languageId;

    private StringFilter name;

    private Boolean distinct;

    public LanguageCriteria() {}

    public LanguageCriteria(LanguageCriteria other) {
        this.languageId = other.languageId == null ? null : other.languageId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LanguageCriteria copy() {
        return new LanguageCriteria(this);
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public LongFilter languageId() {
        if (languageId == null) {
            languageId = new LongFilter();
        }
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LanguageCriteria that = (LanguageCriteria) o;
        return Objects.equals(languageId, that.languageId) && Objects.equals(name, that.name) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageId, name, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageCriteria{" +
            (languageId != null ? "languageId=" + languageId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
