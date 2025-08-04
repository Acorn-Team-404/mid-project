const dropZone = document.querySelector("#dropZone");
    const fileInput = document.querySelector("#fileInput");
    const preview = document.querySelector("#preview");
    const uploadForm = document.querySelector("#uploadForm");

    let selectedFiles = [];

    dropZone.addEventListener("click", () => fileInput.click());

    dropZone.addEventListener("dragover", (e) => {
      e.preventDefault();
      dropZone.classList.add("dragover");
    });

    dropZone.addEventListener("dragleave", () => {
      dropZone.classList.remove("dragover");
    });

    dropZone.addEventListener("drop", (e) => {
      e.preventDefault();
      dropZone.classList.remove("dragover");
      const files = Array.from(e.dataTransfer.files);
      selectedFiles = [...selectedFiles, ...files];
      updatePreview();
    });

    fileInput.addEventListener("change", () => {
      const files = Array.from(fileInput.files);
      selectedFiles = selectedFiles.concat(files);
      updatePreview();
    });

    async function updatePreview() {
      preview.innerHTML = "";
      const dataTransfer = new DataTransfer();

      for (let i = 0; i < selectedFiles.length; i++) {
        const file = selectedFiles[i];
        if (!file.type.startsWith("image/")) continue;

        try {
          const imageUrl = await readFileAsDataURL(file);

          const container = document.createElement("div");
          container.classList.add("preview-item");

          const img = document.createElement("img");
          img.setAttribute("src", imageUrl);

          const btn = document.createElement("button");
          btn.classList.add("remove-btn");
          btn.innerText = "×";
          btn.addEventListener("click", () => {
            selectedFiles.splice(i, 1);
            updatePreview();
          });

          container.appendChild(img);
          container.appendChild(btn);
          preview.appendChild(container);

          dataTransfer.items.add(file);
        } catch (err) {
          console.error("이미지 로딩 실패:", err);
        }
      }

      fileInput.files = dataTransfer.files;
    }

    function readFileAsDataURL(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(file);
      });
    }