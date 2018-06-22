package microservices.cart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")

public class CartResource {

    private static Logger logger = LogManager.getLogger(CartResource.class);

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/all")
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    @GetMapping("/all/cid/{cid}/caller/{caller}")
    public List<Cart> getAll(@PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tcart\t/all/", cid, caller);
        return cartRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cart getOne(@PathVariable int id) {
        Optional<Cart> one = cartRepository.findById(id);
        return one.get();
    }

    @GetMapping("/{id}/cid/{cid}/caller/{caller}")
    public Cart getOne(@PathVariable int id, @PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tcart\t/{}", cid, caller, id);
        Optional<Cart> one = cartRepository.findById(id);
        return one.get();
    }

    @PostMapping("/create/{id}/product/{pid}/cid/{cid}/caller/{caller}")
    public void create(@PathVariable int id, @PathVariable int pid, @PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tcart\t/create/{}/product/{}", cid, caller, id, pid);
        Cart cart = new Cart();
        cart.setId(id);
        cart.setProductId(pid);

        double price = 0.0;
        try {
            logger.info("REQ\t{}\tcart\tproducts\t/price/{}", cid, pid);
            String productUri = "http://localhost:8092/product/price/" + pid + "/cid/" + cid + "/caller/cart";
            RestTemplate restTemplate = new RestTemplate();
            price = restTemplate.getForObject(productUri, Double.class);
            cart.setPrice(price);
        }catch(Exception e){
            logger.info("ERR\t{}\tcart\tproducts\t/price/{}\t{}", cid, pid, e.getMessage());
            }
        Cart savedCart = cartRepository.save(cart);
    }

    //TODO put
    //TODO delete
}
