<template>
  <b-container>
    <br>
    <h4>Add a category</h4>
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
      <b-button type="submit" variant="success">Add Category</b-button>
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
        this.$axios.post('/api/categories', {
          name: this.form.name,
          description: this.form.description
        }).then((() => {
          this.$router.push({name: 'Categories'});
          window.location.reload();
        }));
      } else {
        alert("All fields are required!");
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

  }
}

</script>
<style scoped></style>