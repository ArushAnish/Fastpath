# Fastpath

**Optimization Backbone for Sodium: renderer, chunks, entities, particles, and light updates.**

Fastpath is a performance-focused companion mod designed to supercharge [Sodium](https://github.com/CaffeineMC/sodium-fabric) by optimizing the most demanding aspects of Minecraft’s rendering and simulation pipeline. Acting as a streamlined optimization backbone, Fastpath enhances efficiency across:

- **Renderer** – Reduces redundant calculations and memory overhead for smoother frame delivery.  
- **Chunks** – Parallelized and intelligently scheduled updates to minimize stutter when exploring new terrain.  
- **Entities** – Smarter culling and update routines ensure only relevant entities are processed each frame.  
- **Particles** – Streamlined particle rendering for combat, weather, and large-scale effects without FPS drops.  
- **Lighting** – Reworked light update logic to avoid costly recalculations while keeping visuals accurate.  

Fastpath is not a replacement for Sodium—it’s a reinforcement. By strengthening Sodium’s foundation, it pushes performance further while preserving compatibility and visual quality.

---

## ✨ Features
- Seamless integration with Sodium  
- Reduced frame-time spikes and smoother gameplay  
- Optimized chunk and entity update cycles  
- Efficient particle and lighting management  
- Lightweight, with minimal configuration required  

---

## 📦 Installation
1. Install [Fabric Loader](https://fabricmc.net/) or [Quilt Loader](https://quiltmc.org/).  
2. Download and install [Sodium](https://modrinth.com/mod/sodium).  
3. Place **Fastpath** into your `mods` folder alongside Sodium.  
4. Launch Minecraft and enjoy smoother performance.  

---

## ⚠️ Compatibility
- Requires **Sodium** to function.  
- Designed for modern Fabric/Quilt modpacks.  
- Should be compatible with most optimization mods, but heavy render-altering mods may conflict.  

---

## 🛠️ Roadmap
- Further parallelization of chunk updates  
- Extended support for dynamic lighting mods  
- Configurable optimization profiles  

---

## 🤝 Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to help improve Fastpath.  

---

## 📜 License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.  

---

## 💡 Credits
- Built on the foundation of Sodium by CaffeineMC  
- Inspired by the Minecraft optimization community  

---

**Fastpath** – Because every frame counts.
