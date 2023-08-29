<template>
  <b-container>
    <br>
    <h4>Edit category</h4>
    <b-form @submit.stop.prevent @submit="onSubmit">
      <b-form-group label="Name" label-for="name">
        <b-form-input :state="validatorName" id="name" v-model="form.name" type="text" placeholder="Category name"
                      required></b-form-input>
        <b-form-invalid-feedback :state="validatorName">
          Name cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Description" label-for="description">
        <b-form-input :state="validatorDesc" id="description" v-model="form.description" type="text"
                      placeholder="Description" required></b-form-input>
        <b-form-invalid-feedback :state="validatorDesc">
          Description cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-valid-feedback
          :state="validatorName && validatorDesc">

      </b-form-valid-feedback>
      <br>
      <b-button type="submit" variant="success">Edit Category</b-button>
    </b-form>
  </b-container>
</template>
<script>

export default {
  data() {
    return {
      form: {
        name: '',
        description: ''
      },
      oldName: ''
    }
  },
  methods: {
    onSubmit(e) {
      e.preventDefault();
      if (this.validatorName && this.validatorDesc) {
        this.$axios.post('/api/categories/update/' + this.oldName, {
          name: this.form.name,
          description: this.form.description
        }).then((response => {
          this.$router.push({name: 'Categories'});
        }));
        console.log(this.form);
      } else {
        alert("All fields are required!");
        return;
      }

    }
  },
  computed: {
    validatorName() {
      return this.form.name.length >= 1 && this.form.name.length <= 128;
    },
    validatorDesc() {
      return this.form.description.length >= 3 && this.form.description.length <= 8192;
    }
  },
  mounted() {
    let id = this.$route.params.id;
    this.$axios.get('/api/categories/find/' + id).then((response => {
      const ctg = response.data;
      this.form.name = ctg.name;
      this.oldName = ctg.name;
      this.form.description = ctg.description;
    }));
  }
}

</script>
<style scoped></style>