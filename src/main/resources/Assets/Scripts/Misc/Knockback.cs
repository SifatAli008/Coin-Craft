using System.Collections;
using UnityEngine;

public class Knockback : MonoBehaviour
{
    public bool GettingKnockedBack { get; private set; }

    [SerializeField] private float knockBackTime = 0.2f;

    private Rigidbody2D rb;

    private void Awake()
    {
        rb = GetComponent<Rigidbody2D>();
        if (rb == null)
        {
            Debug.LogError("Knockback script requires a Rigidbody2D component!");
        }
    }

    public void GetKnockedBack(Transform damageSource, float knockBackThrust)
    {
        if (rb == null || damageSource == null) return;

        GettingKnockedBack = true;
        rb.velocity = Vector2.zero;

        Vector2 knockDirection = (transform.position - damageSource.position).normalized * knockBackThrust;
        rb.AddForce(knockDirection, ForceMode2D.Impulse);

        StartCoroutine(KnockRoutine());
    }

    private IEnumerator KnockRoutine()
    {
        yield return new WaitForSeconds(knockBackTime);
        rb.velocity = Vector2.zero;
        GettingKnockedBack = false;
    }
}
