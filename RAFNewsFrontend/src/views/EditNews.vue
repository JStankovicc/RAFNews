<template>
  <b-container>
    <br>
    <h4>Add news</h4>
    <b-form @submit.stop.prevent @submit="onChange">
      <b-form-group label="Title" label-for="title">
        <b-form-input :state="validatorTitle" id="title" v-model="form.title" type="text" placeholder="Title"
                      required></b-form-input>
        <b-form-invalid-feedback :state="validatorTitle">
          Title cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Category" label-for="category">
        <b-form-select :state="validatorCategory" v-model="selected" :options="options"
                       id="category"></b-form-select>
      </b-form-group>
      <b-form-group label="Content" label-for="content">
        <b-form-textarea :state="validatorContent" id="content" v-model="form.content" placeholder="Content"
                         rows="3" max-rows="16"></b-form-textarea>
        <b-form-invalid-feedback :state="validatorContent">
          Content cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group>
        <label for="tags-basic">Tags</label>
        <b-form-tags :state="validatorTags" input-id="tags-basic" v-model="value"></b-form-tags>
      </b-form-group>
      <b-form-valid-feedback :state="validatorContent && validatorTitle && validatorCategory && validatorTags">
      </b-form-valid-feedback>
      <br>
      <b-button type="submit" variant="success">Edit News</b-button>
    </b-form>
  </b-container>
</template>
<script>
export default {
  data() {
    return {
      form: {
        title: '',
        content: ''
      },
      selected: null,
      options: [],
      value: [],
      nID: null,
      toAddTags: [],
      oldTags: []
    }
  },
  computed: {
    validatorContent() {
      return this.form.content.length >= 1 && this.form.content.length <= 8192;
    },
    validatorTitle() {
      return this.form.title.length >= 1 && this.form.title.length <= 128;
    },
    validatorCategory() {
      return this.selected != null;
    },
    validatorTags() {
      return this.value.length != 0;
    }
  },
  mounted() {
    this.$axios.get('/api/categories').then((response => {
      const categories = response.data;
      this.options.push(new Object({
        value: null,
        text: 'Select a category'
      }));
      for (const ctg of categories) {
        this.options.push(new Object({
          value: ctg.id,
          text: ctg.name
        }));
      }
    }));
    let id = this.$route.params.id;
    this.nID = id;
    this.$axios.get('/api/news/find/' + id).then((response => {
      const n = response.data;
      this.form.title = n.title;
      this.selected = n.category_id;
      this.form.content = n.content;
      for (const tag of n.tags) {
        this.value.push(tag.tag);
        this.oldTags.push(tag.tag);
      }
    }));
  },
  methods: {
    onChange(e) {
      e.preventDefault();
      let toAdd = [];
      let toRemove = [];
      for (const v of this.value) {
        if (!this.oldTags.includes(v)) {
          toAdd.push(v);
        }
      }
      for (const o of this.oldTags) {
        if (!this.value.includes(o)) {
          toRemove.push(o);
        }
      }
      let id = this.$route.params.id;
      this.$axios.post('/api/news/update/' + id, {
        title: this.form.title,
        content: this.form.content,
        category_id: this.selected
      }).then((response => {
        const n = response.data;
        for (const tag of toAdd) {
          this.$axios.get('/api/tags/find/' + tag).then((r2 => {
            const tt = r2.data;
            if (tt.id != null) {
              let i = this.$route.params.id;
              this.$axios.get('/api/newstag/add/' + this.nID + '/' + tt.id);
            } else {
              this.$axios.post('/api/tags', {
                tag: tag
              }).then((r3 => {
                const newtag = r3.data;
                let i = this.$route.params.id;
                this.$axios.get('/api/newstag/add/' + this.nID + '/' + newtag.id);
              }));
            }
          }))
        }
        for (const tag of toRemove) {
          this.$axios.get('/api/tags/find/' + tag).then((r2 => {
            const d = r2.data;
            this.$axios.get('/api/newstag/deletetag/' + this.nID + '/' + d.id);
          }));
        }
      })).then((r => {
        this.$router.push({name: 'CreatorNews'});
      }));
    }
  }
}
</script>
<style scoped></style>