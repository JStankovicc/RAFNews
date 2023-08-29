<template>
  <div  v-if="news" class="singlenews">
    <h1>{{ news.title }}</h1>
    <p class="pdate">Objavljeno: {{ news.createdAt }}</p>
    <p class="pauthor">{{ news.author }}</p>
    <p>{{ news.categoryName }}</p>
    <p class="pcontent">{{ news.content }}</p>

    <br>
    <h5>Tags:</h5>
    <button v-for="tag in news.tags" :key="tag.id" class="btn btn-outline-info tagsBtn" @click="goToTag(tag.id)">
      {{ tag.tag }}
    </button>
    <br>
    <br>
    <button class="btn btn-outline-info tagsBtn" @click="likeNews()">
      Likes: {{ computeLikes }}
    </button>

    <button class="btn btn-outline-info tagsBtn" @click="dislikeNews()">
      Dislikes: {{ computeDislikes }}
    </button>

    <button class="btn btn-outline-info tagsBtn" @click="deleteLikeNews()">
      Remove impression.
    </button>
    <br>
    <h5>Leave a comment</h5>
    <b-form @submit.stop.prevent @submit="onSubmit" class="commentsform">
      <b-form-group label="Name" label-for="username">
        <b-form-input :state="validatorUsername" id="username" v-model="form.username" type="text"
                      placeholder="Name" required></b-form-input>
        <b-form-invalid-feedback :state="validatorUsername">
          Name cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Comment:" label-for="comment">
        <b-form-textarea :state="validatorComment" id="comment" v-model="form.comment" type="text"
                         placeholder="Comment" required></b-form-textarea>
        <b-form-invalid-feedback :state="validatorComment">
          Comment cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-valid-feedback :state="validatorUsername && validatorComment">
        Looks good!
      </b-form-valid-feedback>
      <b-button type="submit" variant="success" id="combtn" :disabled="isGood">Post comment</b-button>
    </b-form>
    <h3>Comments</h3>
    <div class="divcomments" v-if="news.comments">
      <div v-for="comment in news.comments" :key="comment.id">
        <h5>{{ comment.author }}</h5>
        <p>{{ comment.text }}<br></p>
        <small class="pdate">{{ comment.postedAt }}</small>

        <button class="btn btn-outline-info tagsBtn" @click="likeComment(comment)">
          Likes: {{ getCommentLikes(comment.id) || 0 }}
        </button>

        <button class="btn btn-outline-info tagsBtn" @click="dislikeComment(comment)">
          Dislikes: {{ getCommentDisikes(comment.id) || 0 }}
        </button>

        <button class="btn btn-outline-info tagsBtn" @click="deleteImpression(comment.id)">
          Remove impression.
        </button>
      </div>
  </div>
    <div>
      <ul id="itemList">
        <li v-for="news in relatedNews" :key="news.id">
          <div class="newsInfoDiv">
            <h3 @click="goToNews(news.id)"><b>{{ news.title }}</b></h3>
            <p>{{ news.content | shortText }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>

import axios from 'axios';

export default {
  name: 'NewsC',
  props: {
    news: {
      type: Object
    }
  },
  data() {
    return {
      form: {
        username: '',
        comment: '',
      },
      noOfLikes: 0,
      noOfDislikes: 0,
      newsId: 1,
      likedNews: 0,
      commentLikes: {},
      commentDislikes: {},
      relatedNews: []

    }
  },
  async mounted() {
    this.getLikes();
    this.getDislikes();
    this.getRelatedNews();
    try{
      const response = await axios.get(`http://localhost:8090/api/comments/news?newsId=${this.$route.params.id}`);
      this.comments = response.data;

      for(const comment of this.comments){
        const likesResponse = await axios.get(`http://localhost:8090/api/like/comment/all?commentId=${comment.id}`);
        const dislikesResponse = await axios.get(`http://localhost:8090/api/like/comment/dislikes/all?commentId=${comment.id}`);
        this.$set(this.commentLikesCount, comment.id, likesResponse.data);
        this.$set(this.commentDislikesCount, comment.id, dislikesResponse.data);
      }
    }catch (error){
      console.error("Not able to get comments:",error);
    }
  },
  filters: {
    shortText(value) {
      if (value.length <= 80) {
        return value;
      } else {
        return value.slice(0, 80) + '...';
      }
    }
  },
  methods: {
    goToNews(id) {
      this.$router.push('/news/' + id);
      window.location.reload();
    },
    getRelatedNews() {
      this.$axios
          .get(`http://localhost:8090/api/news/similartags?newsId=${this.$route.params.id}`)
          .then(response => {
            this.relatedNews = response.data;
          })
          .catch(error => {
            console.error('Error fetching related news:', error);
          });
    },
    getCommentLikes(id){
      this.$axios
          .get(`http://localhost:8090/api/like/comment/all?commentId=${id}`)
          .then((response) => {
            this.noOfLikes = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    getCommentDisikes(id){
      this.$axios
          .get(`http://localhost:8090/api/like/comment/dislikes/all?commentId=${id}`)
          .then((response) => {
            this.noOfLikes = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    async likeComment(comment) {
      try {
        const response = await axios.post(`http://localhost:8090/api/like/comment?commentId=${comment.id}&likeValue=1`);
        this.$set(this.commentLikes, comment.id, response.data.likes);
        window.location.reload();
      } catch (error) {
        console.error("Greška pri lajku komentara:", error);
      }
    },

    async dislikeComment(comment) {
      try {
        const response = await axios.post(`http://localhost:8090/api/like/comment?commentId=${comment.id}&likeValue=-1`);
        this.$set(this.commentDislikes, comment.id, response.data.dislikes);
        window.location.reload();
      } catch (error) {
        console.error("Greška pri dislajku komentara:", error);
      }
    },

    async deleteImpression(commentId) {
      try {
        await axios.delete(`http://localhost:8090/api/like/deleteComment?commentId=${commentId}`);
        this.$delete(this.commentLikes, commentId);
        this.$delete(this.commentDislikes, commentId);
      } catch (error) {
        console.error("Greška pri brisanju impresije:", error);
      }
    },
    deleteLikeNews(){
      this.$axios
          .delete(`/api/like/delete?newsId=${this.$route.params.id}`)
          .then(() => {
            this.getLikes();
            this.getDislikes();
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    likeNews(){
      this.$axios
          .post(`/api/like/news?newsId=${this.$route.params.id}&likeValue=1`)
          .then(() => {
            this.getLikes();
            this.getDislikes();
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    dislikeNews(){
      this.$axios
          .post(`/api/like/news?newsId=${this.$route.params.id}&likeValue=-1`)
          .then(() => {
            this.getLikes();
            this.getDislikes();
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    getLikes(){
      this.$axios
          .get(`/api/like/news/all?newsId=${this.$route.params.id}`)
          .then((response) => {
            this.noOfLikes = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    getDislikes(){
      this.$axios
          .get(`/api/like/news/dislikes/all?newsId=${this.$route.params.id}`)
          .then((response) => {
            this.noOfDislikes = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },

    onSubmit(e) {
      e.preventDefault();
      let id = this.$route.params.id;
      if (!this.validatorComment || !this.validatorUsername) {
        alert("You must enter a name and a comment.")
        return;
      }
      this.$axios.post('/api/comments/' + id, {
        author: this.form.username,
        text: this.form.comment
      }).then((response => {
        this.form.username = '';
        this.form.comment = '';
        const comm = response.data;
        this.news.comments.unshift(comm);
        this.$forceUpdate();
      }))
    },
    goToTag(tag) {
      console.log(tag);
      this.$router.push('/news/withtag/' + tag);
    }
  },
  computed: {
    computeLikes() {
      return this.noOfLikes;
    },
    computeDislikes(){
      return this.noOfDislikes;
    },
    validatorUsername() {
      return this.form.username.length >= 1 && this.form.username.length <= 128;
    },
    validatorComment() {
      return this.form.comment.length >= 1 && this.form.comment.length <= 1024;
    },
    isUEmpty() {
      return this.form.username.length === 0;
    },
    isCEmpty() {
      return this.form.comment.length === 0;
    },
    isGood() {
      return !(this.validatorComment && this.validatorUsername);
    }
  }
}
</script>
<style>
.pdate {
  margin-bottom: 0;
  padding-bottom: 0;
  color: gray;
}

.pauthor {
  font-weight: 600;
}
.related-news {
  margin-top: 8px;
  cursor: pointer;
}

.pcontent {
  max-width: 100%;
  word-wrap: break-word;
}
.divcomments {
  margin-top: 8px;
  margin-bottom: 8px;
  border: 1px solid gray;
  width: 100%;
  margin-bottom: 8px;
  cursor: pointer;
}

.commentsform {
  padding-bottom: 32px;
}

.singlenews {
  padding-top: 24px;
}

.tagsBtn {
  margin-right: 8px;
}
</style>